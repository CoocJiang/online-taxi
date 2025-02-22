package org.example;


import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Data
public class AliPayConfig {

    private String appId = "9021000137691228";

    private String appPrivateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDY4bf8FIMu1lKWplcm6/GA4sZRlztsEykozONXS3vJ6H8ZFAsm49BuXXWKbc3Kn6/Q0h22x0jb/sHQIi8vtzMu2kbK2I00i9oYFQBzJBjsiOyja9m70iNDeCm7QNApDr7hi89MTeUG0JDyiN9OP/yJ2g3a6zJhzJCSK70/aNlo/5dWqmc4yFKFL0VWA/P4nXBUJ1ddrFRBTPm9/TisCykYZ9nqjqE5ZSp8Dw/oBJykLJOVelR1+hU5MK9wXw24oMWGp9p4aBbWlvxRwRkVALnDZv0EvjVa0TCDfrCggPl47K504QmDn7hKdZKIyLbU4nBw9KHOmuEnxIbMLPTD/3EnAgMBAAECggEBALzyqV+sXz2G6xzkfjkihZxL9YtPvi1B5DHvjoCW+pnFPDWVCMIw532/Xo/jac6FoJ7E7641oHxJToENxx5C9Qx0jgha8Wo+DRu98E5fvHcWM1OMunyvbplxLqo8pR2gAxxsuYU4t7dgLWefZyy9Aj73AesfzRz3I1y1ToLXzdFygJsh9hlfiIbGT6V1gzxne1MKQa6gxKrMXI6JiQ8NJMiw4BPKdFHxyEM5+coxVg02gFk8neSG42fFjfyYLG6VD+352Q7pAcTkm7+VcJSU9ynZIbM0/Ui0w++faYjciQxtSuEVEU1xSkRl4LZbpIkqv5JKQELXU5Ah/+upV2DIlEECgYEA8vyfdLnnCl2d4vCpv7WTaoMcS/olGiCY3vt+km73Hoh6/YXfT+cnNX945zxynYtpQZB0AG1L4WhtosUx3U8de2M04+r1SaEJqhS6zylTHkounAuMRXbhCl1CApivk0/oGIb04X6ByuDzSeZQGNBBa3T5lxGgGGv1ZNRkvBwAnzUCgYEA5H8xJIS8wh4/QB+ru3HxtFiIYwy2mIUiHnD427edwzpHkjr1FUrA+mFnywhHWeODKTOPmro2b0sAJShnptKagPgcYPAUf3QmxIkP+Qapt3T8fzEfEUuOPZaZ09XuqPHbspipP8pHiYa0uqT/lLTSn6FwJOZpICZaKYv9JqPoDmsCgYEA6FmA8NFDiLn0XCndaCBtEpDE4jIUgGNjSvYaFlRdoajIUt1MqM9Uwudd6V/4CYZh4/wJsM7SnHyLOfZutD0O6An5bWgjnYCMCUNBTw8pF/G/mB/Cxjni/1Il6O2wFqF2Qt3qFCYwXpu0iJnfRZr+s45rjHWiLTgus3gVy3rLbXECgYB32l+kkBuSltsGf5ZM1A+8UVP44IpGpk6nOQrP9RhDdyZjhZlYecWPj9gpn0YN2FDMI8eHmXVEG0sQMolr77wrOflov4WA9SQAowRF00DigfKPpOxu3frWQ1Cc1PJCm9ppa7obQ7cSZB4YaMPG9LImGUABmvo5auSQNmWrBv6J0QKBgQDH6avZTcEm2+KLtVCF1Sfub8xNpcqoQuJm/p+3KPzSTnd3GQdbU76zqHK2xaxQh0u27SomU60JmblRDw1pNnm5fCV8X0U8x6MOcVBfm+FKZLdl6FJwAtro62FHO8iaA1sDsgyqHDTEzR5uBKFz1oLu/4iqwTigp3cQdpEpW5Xtgw==";

    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkWwZI9NcsRHnBF01Ry6MGwTOZ84UV/cbo2dJcCIt+8flGjPZNQR3DUIJwfYJI8o2Jy1kFVYcLPLn3QsJ97L+daTPpgTYJYF0lzreHigcC7fhCtcVrNV8IWq33BeGifgKyOwrjobHQIDtwpBx0uSMr7At6WRgJePJlIncEE+jqCD/QUBEHE9+nz6C2ABTKus9jCZ8ynUY0v/Qr6NogyS1Hax0Ije9Ku0WZMUycnBtWmijdvSnjGzKrv/Ej20gsaQ9R2swPbAZIwUc8zyaMqmraladtu3JlZIl2xQcf+VpEA2/NbdJUjJqPVdY3bv0yDmzYa8f2anw/v5Hvio6T9lCCQIDAQAB";

    private String notifyUrl = "https://819io41ju102.vicp.fun/alipay/notify";

    //支付成功后的回调地址
    private String returnUrl = "https://819io41ju102.vicp.fun/test";

    @PostConstruct
    public void  init(){
        // 设置参数（全局只需设置一次）
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";
        config.appId = this.appId;
        config.merchantPrivateKey = this.appPrivateKey;
        config.alipayPublicKey = this.publicKey;
        config.notifyUrl = this.notifyUrl;
        Factory.setOptions(config);
        System.out.println("=======支付宝SDK初始化成功=======");
    }

}
