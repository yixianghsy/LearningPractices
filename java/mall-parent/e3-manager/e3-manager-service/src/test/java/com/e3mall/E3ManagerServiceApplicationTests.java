package com.e3mall;

import com.e3mall.pojo.EasyUITreeNode;
import com.e3mall.mansger.service.ItemCatService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.ByteBuffer;
import java.util.List;

@SpringBootTest
class E3ManagerServiceApplicationTests {
	@Reference
	private ItemCatService itemCatServiceImpl;

	private static ByteBuffer buffer = ByteBuffer.allocate(8);

	@Test
	void contextLoads() {
		List<EasyUITreeNode> itemCatlist = itemCatServiceImpl.getItemCatlist(0);
		System.out.println(itemCatlist.get(0));
	}
	@Test
	public void tests(){
		System.out.println("123456789".getBytes());
		System.out.println(longToBytes(12346789));
		System.out.println(bytesToLong(longToBytes(12346789)));
	}
	//byte 数组与 long 的相互转换
	public static byte[] longToBytes(long x) {
		buffer.putLong(0, x);
		return buffer.array();
	}

	public static long bytesToLong(byte[] bytes) {
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();//need flip
		return buffer.getLong();
	}
}
