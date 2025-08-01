#!/usr/bin/env python3

import subprocess
import re
import sys
import argparse

VERSION_PATTERN = re.compile(r'^\d+\.\d+$')


def get_patch_count() -> str:
    result = subprocess.check_output(
        ["git", "rev-list", "--count", "HEAD"],
        text=True
    )
    return result.strip()

def get_version(source: str) -> str:
    if source.startswith("git:"):
        try:
            return subprocess.check_output(["git", "show", source[4:]], text=True).strip()
        except subprocess.CalledProcessError:
            return "0.0"
    else:
        with open(source) as f:
            return f.read().strip()

def parse_version(v: str):
    if not VERSION_PATTERN.match(v):
        raise ValueError(f"Invalid version format: {v}")
    return tuple(map(int, v.split('.')))

def version_file_changed() -> bool:
    try:
        changed_files = subprocess.check_output(
            ["git", "diff", "--name-only", "HEAD^", "HEAD"],
            text=True
        ).splitlines()
        return "VERSION" in changed_files
    except subprocess.CalledProcessError:
        return False

def main():
    parser = argparse.ArgumentParser(description="Tag new versions on valid MAJOR.MINOR bumps.")
    parser.add_argument("--dry-run", action="store_true", help="Check what would happen, but don't tag.")
    parser.add_argument("--force-tag", action="store_true", help="Force tag if VERSION changed, skipping checks.")
    args = parser.parse_args()

    if not version_file_changed():
        print("VERSION file not changed; skipping version check.")
        return

    current_str = get_version("VERSION")
    previous_str = get_version("git:HEAD^:VERSION")

    try:
        current = parse_version(current_str)
        previous = parse_version(previous_str)
    except ValueError as e:
        print(f"ERROR: {e}")
        sys.exit(1)

    patchlevel = get_patch_count()
    tag = f"v{current_str}.{patchlevel}"

    if args.force_tag:
        print(f"Force tagging {tag} (no version bump check).")
    else:
        major_delta = current[0] - previous[0]
        minor_delta = current[1] - previous[1]

        if major_delta == 1 and current[1] == 0:
            bump_type = "major"
        elif major_delta == 0 and minor_delta == 1:
            bump_type = "minor"
        else:
            print("ERROR: Invalid version bump:")
            print(f"  Previous: {previous_str}")
            print(f"  Current:  {current_str}")
            print("Expected:")
            print("  - Major +1 and Minor reset to 0")
            print("  - OR Minor +1 and Major unchanged")
            if args.dry_run:
                sys.exit(0)
            else:
                sys.exit(1)

        print(f"Valid {bump_type} version bump. Preparing to tag {tag}.")

    if args.dry_run:
        print(f"[DRY RUN] Would tag {tag}")
    else:
        print(f"Tagging {tag}")
        subprocess.run(["git", "tag", tag], check=True)
        try:
            subprocess.run(["git", "push", "origin", tag], check=True)
        except subprocess.CalledProcessError as e:
            if "403" in str(e) or "Write access to repository not granted" in str(e):
                print(f"\nERROR: GitHub rejected the tag push — likely due to GITHUB_TOKEN lacking write permissions.")
                print(f"\nTo fix:")
                print(f"  1. Go to your GitHub repo: Settings -> Actions → Workflow permissions")
                print(f"  2. Switch to: 'Read and write permissions'")
                print(f"  3. Save the setting, then re-run this workflow")
                sys.exit(1)
            else:
                raise  # re-raise unexpected errors
if __name__ == "__main__":
    main()

