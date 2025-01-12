package com.dojinyou.devcourse.voucherapplication.voucher;

import com.dojinyou.devcourse.voucherapplication.Response;
import com.dojinyou.devcourse.voucherapplication.VoucherApplication;
import com.dojinyou.devcourse.voucherapplication.voucher.domain.Voucher;
import com.dojinyou.devcourse.voucherapplication.voucher.domain.VoucherAmount;
import com.dojinyou.devcourse.voucherapplication.voucher.domain.VoucherMapper;
import com.dojinyou.devcourse.voucherapplication.voucher.domain.VoucherType;
import com.dojinyou.devcourse.voucherapplication.voucher.dto.VoucherRequest;
import com.dojinyou.devcourse.voucherapplication.voucher.dto.VoucherResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = VoucherApplication.class)
class VoucherControllerTest {
    private static final String ERROR_MESSAGE_ABOUT_REFLEXTION = "reflextion 과정에서 에러가 발생하였습니다.\n";

    @Autowired
    VoucherController voucherController;

    @MockBean
    VoucherService voucherService;

    @Nested
    @DisplayName("Create mehotd에 관하여")
    class Describe_create_method {
        @Nested
        @DisplayName("잘못된 DTO가 들어온다면,")
        class Context_Illegal_VoucherCreateDTo {

            @ParameterizedTest
            @NullSource
            @DisplayName("예외를 발생시킨다.")
            void it_throws_Exception(VoucherRequest voucherRequest) {
                // given

                // when
                Throwable thrown = catchThrowable(() -> voucherController.create(voucherRequest));

                // then
                assertThat(thrown).isNotNull();
                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_Exception() {
                // given
                VoucherRequest voucherRequest = new VoucherRequest(null, null);

                // when
                Throwable thrown = catchThrowable(() -> voucherController.create(voucherRequest));

                // then
                assertThat(thrown).isNotNull();
                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

        }

        @Nested
        @DisplayName("정상적인 DTO가 들어온다면,")
        class Context_Correct_VoucherCreateDTo {

            @ParameterizedTest
            @EnumSource(VoucherType.class)
            @DisplayName("Voucher Service의 create 함수를 호출한다.")
            void it_Call_of_VoucherService_create_method(VoucherType voucherType) {
                // given
                int voucherAmount = 50;
                VoucherRequest voucherRequest = new VoucherRequest(voucherType, VoucherAmount.of(voucherType, voucherAmount));

                Long id = 999_999_999L;
                try {
                    Method getDomainMethod = VoucherMapper.class.getDeclaredMethod("getDomain", Long.class, VoucherType.class, VoucherAmount.class);
                    getDomainMethod.setAccessible(true);
                    Voucher voucherWithId = (Voucher) getDomainMethod.invoke(null, id, voucherRequest.getType(), voucherRequest.getAmount());

                    when(voucherService.create(any(Voucher.class))).thenReturn(voucherWithId);
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    fail(ERROR_MESSAGE_ABOUT_REFLEXTION + e.getMessage());
                }

                // when
                voucherController.create(voucherRequest);

                // then
                verify(voucherService, atLeastOnce()).create(any());

            }

            @ParameterizedTest
            @EnumSource(VoucherType.class)
            @DisplayName("ResponseDto를 가진 Response를 return한다.")
            void it_throws_Exception(VoucherType voucherType) {
                // given
                Long id = 999_999_999L;
                int voucherAmount = 50;
                LocalDateTime testTime = LocalDateTime.of(2022, 5, 17,
                                                          0,17,0, 0);
                VoucherRequest voucherRequest = new VoucherRequest(voucherType, VoucherAmount.of(voucherType, voucherAmount));
                Voucher voucherWithId = Voucher.of(id,
                                                   voucherRequest.getType(),
                                                   voucherRequest.getAmount(),
                                                   testTime, testTime);

                when(voucherService.create(any(Voucher.class))).thenReturn(voucherWithId);

                VoucherResponse expectedResponseDto = new VoucherResponse(id,
                                                                          voucherWithId.getType(),
                                                                          voucherWithId.getAmount(),
                                                                          voucherWithId.getCreatedAt(),
                                                                          voucherWithId.getUpdatedAt());

                // when
                Response<VoucherResponse> response = voucherController.create(voucherRequest);
                VoucherResponse responseDto = response.getData();

                // then
                assertThat(response).isNotNull();
                assertThat(response.getState()).isEqualTo(Response.State.SUCCESS);
                assertThat(responseDto).isNotNull();
                assertThat(responseDto.getId()).isEqualTo(expectedResponseDto.getId());
                assertThat(responseDto.getType()).isEqualTo(expectedResponseDto.getType());
                assertThat(responseDto.getAmount()).isEqualTo(expectedResponseDto.getAmount());
            }
        }
    }

    @Nested
    @DisplayName("findAll mehotd에 관하여")
    class Describe_findAll_method {
        @Nested
        @DisplayName("함수가 호출되었을 때,")
        class Context_MethodCall {

            @Test
            @DisplayName("Voucher Service의 findAll 함수를 호출한다.")
            void it_Call_of_VoucherService_findAll_method() {
                // given
                when(voucherService.findAll()).thenReturn(Arrays.asList(new Voucher[]{}));

                // when
                voucherController.findAll();

                // then
                verify(voucherService, atLeastOnce()).findAll();


            }

            @Test
            @DisplayName("List<VoucherResponse>를 가진 Response를 return한다.")
            void it_throws_Exception() {
                // given
                when(voucherService.findAll()).thenReturn(Arrays.asList(new Voucher[]{}));

                // when
                Response<List<VoucherResponse>> response = voucherController.findAll();

                // then
                assertThat(response).isNotNull();
                assertThat(response.getState()).isEqualTo(Response.State.SUCCESS);
                assertThat(response.getData()).isNotNull();
            }
        }
    }

}