package com.ElectricityAutomationInitiative;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sun.security.util.Password;

public class Temp {
    public static void main(String[] args) {
        PasswordEncoder passwordEncode= new BCryptPasswordEncoder();
        System.out.println(passwordEncode.encode("123aaa"));
        System.out.println(passwordEncode.encode("123qax"));
        System.out.println(passwordEncode.encode("city2"));
        System.out.println(passwordEncode.encode("city3"));
        System.out.println(passwordEncode.encode("city4"));

    }
}
//$2a$10$KjkG05JhKzW8ff1tDspkj.pGWi5ls/cv0MGiogrZW4sObXc27eaPO
//$2a$10$KjkG05JhKzW8ff1tDspkj.pGWi5ls/cv0MGiogrZW4sObXc27eaPO
//$2a$10$0ThV0/5/tdQuKbUG6tSBW.DZM/FW5ScRl4G.gu6Oj1rwflGGD7/mO
//1234=$2a$10$zo.aCBDw6GKI.Jp23FvME.I0T33Cn5BRvZ.B7QQtQE8yeBOl7n16K
//$2a$10$Opxi2XqUzipReqgkm9muNeuJq3aTklARQTLymn6OlgM5mBGyZ4SR.
//aaaa=$2a$10$I.Q8hdsa85BbgzDkob3/..Iyh0.ChNlAyJIfQy1VENT0IT3tln00.
//aaaa=$2a$10$zo.aCBDw6GKI.Jp23FvME.I0T33Cn5BRvZ.B7QQtQE8yeBOl7n16K
//$2a$10$I.Q8hdsa85BbgzDkob3/..Iyh0.ChNlAyJIfQy1VENT0IT3tln00.
//1234=$2a$10$VJ9c1wL3suNOXYWsRfg.Qeipz1OMqbIoLUbbxKieUbggH0UFly8Ha
//1234=$2a$10$lxlo9ZLkFPfzUq0NXmRZBOehi/g0cA9yk4.CuvjB7esby4Ju3zuk6