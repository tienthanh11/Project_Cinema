package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.entity.AccountRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountRoleService{
//    void createAccountRole(Long  accountId , Long roleId);

    void createAccountRole(long accountId, long roleId);
    List<AccountRole> findAllByAccountUsername(String username);

    public static void main(String[] args) {
        String s1 = "aabbcc";
        String s2 = "aabbcc";
        System.out.println(findCharCommon(s1,s2));
        String a = "abcd";
        String b = "abcd";
        String c = new String("abcd");
        String d = new String("abcd");
        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(c == d);
    }
    public static int findCharCommon(String s1,String s2) {
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++){
                if (s1.charAt(i)==s2.charAt(j)){
                    s1=s1.replaceFirst(Character.toString(s1.charAt(i)),"");
                    count++;
                }
            }
        }
        return count;

    }
}
