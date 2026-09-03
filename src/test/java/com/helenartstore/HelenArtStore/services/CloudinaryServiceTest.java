package com.helenartstore.HelenArtStore.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CloudinaryServiceTest {

    @Test
    void uploadImageReturnsNullWhenCloudinaryUploadFails() throws IOException {
        Cloudinary cloudinary = mock(Cloudinary.class);
        Uploader uploader = mock(Uploader.class);

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new RuntimeException("No such host is known"));

        CloudinaryService service = new CloudinaryService(cloudinary);
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "sample.jpg",
                "image/jpeg",
                "sample-content".getBytes());

        assertNull(service.uploadImage(file));
    }
}
