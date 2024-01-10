package com.example.spring_project.login;

import com.example.spring_project.facade.ClientFacade;
import com.example.spring_project.facade.CompanyFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


@Service
public class LoginManager {
    private ApplicationContext ctx;

    public LoginManager(ApplicationContext ctx) {
        this.ctx = ctx;
    }


    public ClientFacade login(String email, String password, ClientType clientType) {
        switch (clientType){
            case Administrator:

                break;
            case Company:
                CompanyFacade companyFacade =ctx.getBean(CompanyFacade.class);
                if (companyFacade.login(email,password))
                    return companyFacade;
                break;
            case Customer:

                break;
        }
        return null;
    }
}
