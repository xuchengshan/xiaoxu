package com.example.springbootdemo.test;

import com.example.springbootdemo.lombok.ChildrenDTO;
import io.lettuce.core.ScriptOutputType;
import org.apache.curator.shaded.com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> orgs = Lists.newArrayList("A-D", "A-D-E", "A-D-E-L", "G-I");
// 按字符串长度从短到长排序
        orgs.sort(Comparator.comparingInt(String::length));

        List<String> result = new ArrayList<>();

        for (int i = orgs.size() - 1; i >= 0; i--) {
            String org = orgs.get(i);
            boolean isChildrenOrg = false;

            // 遍历剩下的组织编号
            for (int j =0; j < i; j++) {
                if (org.startsWith(orgs.get(j))) {
                    isChildrenOrg = true;
                    break;
                }
            }

            // 如果没有子组织，加入结果集
            if (!isChildrenOrg) {
                result.add(org);
            }
        }
        System.out.println(result);
    }
}
