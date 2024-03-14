package com.paulbehofsics.dirscanner.rest;

import com.paulbehofsics.dirscanner.core.dtos.GetFileSizeDto;
import com.paulbehofsics.dirscanner.core.services.FileSizeService;
import com.paulbehofsics.dirscanner.rest.controllers.FileSizeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileSizeController.class)
@EntityScan(basePackages = {"com.paulbehofsics.dirscanner"})
public class FileSizeControllerTest {
	private final String BASE_URL = "/filesizes";

	@Autowired
	private MockMvc mvc;

	@MockBean(reset = MockReset.AFTER)
	private FileSizeService fileSizeService;

	@Test
	public void givenDirectoryFromService_whenGetFileSizes_thenReturnDirectory() throws Exception {
		// GIVEN
		final String DIR_PATH = "/root/dir";
		final String DIR_NAME = "dir";
		final int DIR_SIZE_INT = 4000;
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		final GetFileSizeDto fileSizeDto = new GetFileSizeDto(DIR_PATH, DIR_NAME,
				(long) DIR_SIZE_INT, DATETIME_NOW, DATETIME_NOW);
		given(fileSizeService.getFileSizes()).willReturn(List.of(fileSizeDto));

		// WHEN
		mvc.perform(get(BASE_URL))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].path", is(DIR_PATH)))
				.andExpect(jsonPath("$[0].name", is(DIR_NAME)))
				.andExpect(jsonPath("$[0].size", is(DIR_SIZE_INT)));

		// THEN
		verify(fileSizeService, atLeastOnce()).getFileSizes();
	}

	@Test
	public void givenDirectoryFromService_whenGetFileSizesWithFiletype_thenReturnDirectory() throws Exception {
		// GIVEN
		final String DIR_PATH = "/root/dir";
		final String DIR_NAME = "dir";
		final int DIR_SIZE_INT = 4000;
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		final GetFileSizeDto fileSizeDto = new GetFileSizeDto(DIR_PATH, DIR_NAME,
				(long) DIR_SIZE_INT, DATETIME_NOW, DATETIME_NOW);
		given(fileSizeService.getFileSizes(any())).willReturn(List.of(fileSizeDto));

		// WHEN
		mvc.perform(get(BASE_URL + "?filetype=txt"))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].path", is(DIR_PATH)))
				.andExpect(jsonPath("$[0].name", is(DIR_NAME)))
				.andExpect(jsonPath("$[0].size", is(DIR_SIZE_INT)));

		// THEN
		verify(fileSizeService, atLeastOnce()).getFileSizes(any());
	}
}
