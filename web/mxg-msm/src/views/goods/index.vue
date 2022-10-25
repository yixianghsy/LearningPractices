<template>
  <div>
    <!-- :inline="true" 表单一行显示 -->
    <el-form
      ref="searchForm"
      :inline="true"
      :model="searchMap"
      style="margin-top: 20px"
    >
      <!-- 重置会看 el-form-item 组件元素的 prop 上是否指定了字段名，指定了才会重置生效 -->
      <el-form-item prop="name">
        <el-input
          v-model="searchMap.name"
          placeholder="商品名称"
          style="width: 200px"
        ></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          v-model="searchMap.code"
          placeholder="商品编号"
          style="width: 200px"
        ></el-input>
      </el-form-item>
      <el-form-item prop="supplierName">
        <!-- <el-input
          readonly
          @click.native="dialogSupplierVisible = true"
          v-model="searchMap.supplierName"
          placeholder="选择供应商"
          style="width: 200px"
        ></el-input> -->
        <el-input
          readonly
          @click.native="dialogSupplierVisible = true"
          v-model="searchMap.supplierName"
          placeholder="选择供应商"
          style="width: 200px"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchData">查询</el-button>
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button @click="$refs['searchForm'].resetFields()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 列表 -->
    <el-table
      :data="list"
      height="380"
      border-style="width: 100%"
      highlightcurrent-row
    >
      <el-table-column type="index" label="序号" width="60"></el-table-column>
      <el-table-column prop="name" label="商品名称"></el-table-column>
      <el-table-column prop="code" label="商品编码"></el-table-column>
      <el-table-column prop="spec" label="商品规格"></el-table-column>
      <el-table-column prop="retailPrice" label="零售价"></el-table-column>
      <el-table-column prop="purchasePrice" label="进货价"></el-table-column>
      <el-table-column prop="storageNum" label="库存数量"></el-table-column>
      <el-table-column prop="supplierName" label="供应商"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row.id)"
            >编辑</el-button
          >
          <el-button size="mini" type="danger" @click="handleDele(scope.row.id)"
            >删 除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页，添加在div里面 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
    <!-- 选择供应商对话框 -->
    <el-dialog
      title="选择供应商"
      :visible.sync="dialogSupplierVisible"
      width="500px"
    >
      <supplier @option-supplier="optionSupplier" :isDialog="true"></supplier>
    </el-dialog>
  </div>
</template>
<script>
import goodsApi from "@/api/goods";
// Supplier 作为子组件
import Supplier from "@/views/supplier";
export default {
  data() {
    return {
      list: [],
      total: 0, // 总记录数
      currentPage: 1, // 当前页, 默认第1页
      pageSize: 10, // 每页显示条数， 10条
      searchMap: {}, // 条件查询绑定条件值
      searchMap: {
        name: "",
        code: "",
        supplierName: "",
      },
      dialogSupplierVisible: false, // 控制供应商对话框
    };
  },
  // 不要忘记注册
  components: { Supplier },
  created() {
    this.fetchData();
  },
  methods: {
    optionSupplier(currentRow) {
      // currentRow 子组件传递的数据
      console.log("parent", currentRow);
      this.searchMap.supplierName = currentRow.name; // 供应商名称
      this.searchMap.supplierId = currentRow.id; // 供应商ID
      this.dialogSupplierVisible = false; //关闭窗口
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchData();
    },
    fetchData() {
      goodsApi
        .search(this.currentPage, this.pageSize, this.searchMap)
        .then((response) => {
          const reqs = response.data;
          this.total = reqs.data.total;
          this.list = reqs.data.rows;
          //console.log(this.list)
        });
    },
  },
};
</script>