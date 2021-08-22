package W3D2.jcu;

import W3D2.jcu.voucher.VoucherService;
import java.io.IOException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class JcuApplication {

	public static void main(String[] args) throws IOException {

		// enum 추가전 version 임
		// 새로운 커멘드 delete 추가해보기
		ApplicationContext ac = new AnnotationConfigApplicationContext(JcuApplication.class);
		var voucherService = ac.getBean(VoucherService.class);
		new CommandLineApplication(voucherService);
	}

}