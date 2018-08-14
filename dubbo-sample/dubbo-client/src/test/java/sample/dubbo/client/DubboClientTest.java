package sample.dubbo.client;

import cn.sccfc.basebiz.members.membersws.api.MemberService;
import cn.sccfc.basebiz.members.membersws.api.contract.SignUpUserInfoRequest;
import cn.sccfc.basebiz.members.membersws.api.contract.SignUpUserInfoResponse;
import cn.sccfc.usercenter.scuserauthws.api.UserAuthService;
import cn.sccfc.usercenter.scuserauthws.api.contract.LoginByPwdRequest;
import cn.sccfc.usercenter.scuserauthws.api.contract.LoginByPwdResponse;
import cn.sccfc.usercenter.usercertification.api.UserCertificationService;
import cn.sccfc.usercenter.usercertification.api.contract.GetRealNameInfoRequest;
import cn.sccfc.usercenter.usercertification.api.contract.GetRealNameInfoResponse;
import cn.sccfc.usercenter.usercertification.api.contract.SaveRealNameInfoRequest;
import cn.sccfc.usercenter.usercertification.api.contract.SaveRealNameInfoResponse;
import cn.sccfc.usercenter.usercertification.api.contract.dtotypes.UserInfo;
import com.alibaba.dubbo.common.json.JSON;
import com.allinfinance.adm.service.api.CustomMappingOperationService;
import com.allinfinance.adm.service.api.LookupCustomIdService;
import com.allinfinance.adm.service.bean.CustomMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class DubboClientTest {
    //@Autowired
    private CustomMappingOperationService customMappingOperationService;
    //@Autowired
    private LookupCustomIdService lookupCustomIdService;
    //@Autowired
    MemberService memberService;
    @Autowired
    UserAuthService userAuthService;
    @Autowired
    UserCertificationService userCertificationService;
    @Test
    public void test() {

    }

    @Test
    public void testDubboClient() {
        List<CustomMapping> customMappings = new ArrayList<>();
        CustomMapping customMapping = new CustomMapping();
        customMapping.setKeyType("I");
        customMapping.setKeyValue(new Random().nextInt() + "-VALUE");
        customMapping.setCustomName("customName");
        customMapping.setCustId(new Random().nextInt() + "");
        customMapping.setIdentityType("ID");
        customMapping.setIdentityCard("");
        customMappings.add(customMapping);
        //customMappingOperationService.saveCustomMapping(customMappings);


        customMapping.setKeyType("I");
        customMapping.setKeyValue("-1615880242-VALUE");
        String result = lookupCustomIdService.lookupCustomId(customMapping);
        System.out.println(result);
    }

    @Test
    public void testRegister() {
        SignUpUserInfoRequest req = new SignUpUserInfoRequest();
        req.setPhoneNo("13899999999");
        req.setPassword("123456");
        req.setSmsVerifyCode("888888");
        req.setScenario("APP_SIGNIN");
        SignUpUserInfoResponse resp = memberService.signUpUserInfo(req);
        System.out.println(resp.toJson());
    }

    @Test
    public void testLogin() {
        LoginByPwdRequest req = new LoginByPwdRequest();
        req.setUserName("13899999999");
        req.setPassword("123456");
        req.setLoginChannel("1");
        req.setLoginPlatform("1");
        req.setLongitude("1");
        req.setLatitude("1");
        req.setExtraInfo("{}");
        LoginByPwdResponse resp = userAuthService.loginByPwd(req);
        System.out.println(resp);
    }
    //token: cfc064dc-70b7-4ed7-8e3b-385aefeb3086
    //uid: 1000000836
    //guid: 4495351CA22F4DB7B94E3C975C307CEA

    @Test
    public void testSaveRealName() {
        SaveRealNameInfoRequest req = new SaveRealNameInfoRequest();
        UserInfo userInfo = new UserInfo();
        userInfo.setAuthWay("BANKCARD");
        userInfo.setChannelID("1212112");
        userInfo.setCustID("1212112");
        userInfo.setIdNo("1212112");
        userInfo.setIdType(1);
        userInfo.setOpenID("1212112");
        userInfo.setRealName("1212112");
        userInfo.setUid("1212112");
        req.setUserInfo(userInfo);
        SaveRealNameInfoResponse resp = userCertificationService.saveRealNameInfo(req);
        System.out.println(resp);
    }

    @Test
    public void testGetRealName() {
        GetRealNameInfoRequest req = new GetRealNameInfoRequest();
        req.setChannelID("QD0004");
//        req.setCustID();
        req.setOpenID("4495351CA22F4DB7B94E3C975C307CEA1");
//        req.setUid();
        GetRealNameInfoResponse resp = userCertificationService.getRealNameInfo(req);
        System.out.println(resp);
    }
}
