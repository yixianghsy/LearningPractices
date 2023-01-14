webpackJsonp([18],{"6gsm":function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=n("bUp0"),a={name:"menuList",data:function(){return{list:null,total:null,listLoading:!0,listQuery:{pageNum:1,pageSize:5},parentId:0}},created:function(){this.resetParentId(),this.getList()},watch:{$route:function(e){this.resetParentId(),this.getList()}},methods:{resetParentId:function(){this.listQuery.pageNum=1,null!=this.$route.query.parentId?this.parentId=this.$route.query.parentId:this.parentId=0},handleAddMenu:function(){this.$router.push("/ums/addMenu")},getList:function(){var e=this;this.listLoading=!0,Object(i.c)(this.parentId,this.listQuery).then(function(t){e.listLoading=!1,e.list=t.data.list,e.total=t.data.total})},handleSizeChange:function(e){this.listQuery.pageNum=1,this.listQuery.pageSize=e,this.getList()},handleCurrentChange:function(e){this.listQuery.pageNum=e,this.getList()},handleHiddenChange:function(e,t){var n=this;Object(i.f)(t.id,{hidden:t.hidden}).then(function(e){n.$message({message:"修改成功",type:"success",duration:1e3})})},handleShowNextLevel:function(e,t){this.$router.push({path:"/ums/menu",query:{parentId:t.id}})},handleUpdate:function(e,t){this.$router.push({path:"/ums/updateMenu",query:{id:t.id}})},handleDelete:function(e,t){var n=this;this.$confirm("是否要删除该菜单","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){Object(i.b)(t.id).then(function(e){n.$message({message:"删除成功",type:"success",duration:1e3}),n.getList()})})}},filters:{levelFilter:function(e){return 0===e?"一级":1===e?"二级":void 0},disableNextLevel:function(e){return 0!==e}}},l={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("el-card",{staticClass:"operate-container",attrs:{shadow:"never"}},[n("i",{staticClass:"el-icon-tickets",staticStyle:{"margin-top":"5px"}}),e._v(" "),n("span",{staticStyle:{"margin-top":"5px"}},[e._v("数据列表")]),e._v(" "),n("el-button",{staticClass:"btn-add",attrs:{size:"mini"},on:{click:function(t){e.handleAddMenu()}}},[e._v("\n      添加\n    ")])],1),e._v(" "),n("div",{staticClass:"table-container"},[n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],ref:"menuTable",staticStyle:{width:"100%"},attrs:{data:e.list,border:""}},[n("el-table-column",{attrs:{label:"编号",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.id))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"菜单名称",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.title))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"菜单级数",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e._f("levelFilter")(t.row.level)))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"前端名称",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.name))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"前端图标",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(e){return[n("svg-icon",{attrs:{"icon-class":e.row.icon}})]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"是否显示",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-switch",{attrs:{"active-value":0,"inactive-value":1},on:{change:function(n){e.handleHiddenChange(t.$index,t.row)}},model:{value:t.row.hidden,callback:function(n){e.$set(t.row,"hidden",n)},expression:"scope.row.hidden"}})]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"排序",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(t.row.sort))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"设置",width:"120",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{size:"mini",type:"text",disabled:e._f("disableNextLevel")(t.row.level)},on:{click:function(n){e.handleShowNextLevel(t.$index,t.row)}}},[e._v("查看下级\n          ")])]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"操作",width:"200",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(n){e.handleUpdate(t.$index,t.row)}}},[e._v("编辑\n          ")]),e._v(" "),n("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(n){e.handleDelete(t.$index,t.row)}}},[e._v("删除\n          ")])]}}])})],1)],1),e._v(" "),n("div",{staticClass:"pagination-container"},[n("el-pagination",{attrs:{background:"",layout:"total, sizes,prev, pager, next,jumper","page-size":e.listQuery.pageSize,"page-sizes":[10,15,20],"current-page":e.listQuery.pageNum,total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange,"update:currentPage":function(t){e.$set(e.listQuery,"pageNum",t)}}})],1)],1)},staticRenderFns:[]};var s=n("VU/8")(a,l,!1,function(e){n("q1Of")},"data-v-4fb35e85",null);t.default=s.exports},q1Of:function(e,t){}});
//# sourceMappingURL=18.55952000ab227f5ee7d7.js.map