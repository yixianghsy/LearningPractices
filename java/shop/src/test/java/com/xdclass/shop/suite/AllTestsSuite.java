/*
 * www.yiji.com Inc.
 * Copyright (c) 2020 All Rights Reserved.
 */

/*
 * 修订记录：
 * muyu@yiji.com 2020-10-30 15:59 创建
 */
package com.xdclass.shop.suite;

import com.xdclass.shop.test.AdminTest;
import com.xdclass.shop.test.OrderTest;
import com.xdclass.shop.test.UserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author daniel
 * @version 2020/10/30
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AdminTest.class, OrderTest.class, UserTest.class})
public class AllTestsSuite {

}
