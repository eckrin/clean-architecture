package com.eckrin.clean_architecture.account.adapter.in.web;

import com.eckrin.clean_architecture.account.application.port.in.SendMoneyCommand;
import com.eckrin.clean_architecture.account.application.port.in.SendMoneyUseCase;
import com.eckrin.clean_architecture.account.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.eckrin.clean_architecture.account.domain.Account.AccountId;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트 - 웹 어댑터 테스트
 * HTTP 통신 대신 Mock객체를 이용하여 테스트했기 때문에 웹 컨트롤러 이하로 실제 제어가 내려가지는 않는다. (실제 트랜잭션이 수행되지 않음)
 * 그렇지만 네트워크와 관련된 일련의 과정을 테스트하기 때문에 통합 테스트로 보는것이 맞다.
 */
@WebMvcTest(controllers = SendMoneyController.class)
class SendMoneyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SendMoneyUseCase sendMoneyUseCase;

	@Test
	void testSendMoney() throws Exception {

		mockMvc.perform(post("/account/send/{sourceAccountId}/{targetAccountId}/{amount}", 41L, 42L, 500)
				.header("Content-Type", "application/json"))
				.andExpect(status().isOk());

		then(sendMoneyUseCase).should()
				.sendMoney(refEq(new SendMoneyCommand(
						new AccountId(41L),
						new AccountId(42L),
						Money.of(500L))
                ));
	}

}
