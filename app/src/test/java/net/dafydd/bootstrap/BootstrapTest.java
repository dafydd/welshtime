package net.dafydd.bootstrap;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BootstrapTest {

    @Test
    void mockitoExampleTest() {
        MyService mockService = Mockito.mock(MyService.class);

        when(mockService.getData()).thenReturn("Mock Data");

        assertEquals("Mock Data", mockService.getData());
        verify(mockService, times(1)).getData();
    }

    interface MyService {
        String getData();
    }
}
