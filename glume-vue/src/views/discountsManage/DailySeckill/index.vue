<!-- TODO 每日秒杀待完成 -->
<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-button icon="el-icon-plus" size="mini" @click="addData">新增</el-button>
        <el-button icon="el-icon-plus" type="danger" size="mini" @click="batchDelete">批量删除</el-button>
        <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
      </el-row>
      <!--    内容展示-->
      <el-table v-loading="loading" :data="dataList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="活动标题" prop="title"/>
        <el-table-column label="开始日期时间" prop="startTime"/>
        <el-table-column label="结束日期时间" prop="endTime"/>
        <el-table-column label="状态" prop="status">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status == 1" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="创建人" prop="username"/>
        <el-table-column label="创建时间" prop="createTime"/>
        <el-table-column label="操作" prop="sort">
          <template slot-scope="scope">
            <el-button icon="el-icon-edit" type="warning" size="mini" @click="amendData(scope.row)">修改</el-button>
            <el-button icon="el-icon-delete" type="danger" size="mini" @click="deleteData(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <!-- 弹出层 -->
    <my-dialog ref="dialog" :title="title" :form="form" @submitForm="submitForm"/>
  </layout-container>
</template>

<script>
  import SearchBox from '@/components/searchBox/index'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  import {MessageBox} from "element-ui";
  import {getData,save,info,update,delData} from "@/api/DiscountsManage/DailySeckill";
  import MyDialog from "@/views/discountsManage/DailySeckill/component/MyDialog";
  export default {
    name: 'index',
    components: {MyDialog, LayoutContainer, SearchBox },
    data() {
      return {
        ids: null,
        total: 0,
        showSearch: true,
        loading: false,
        queryParams: {
          page: 1,
          limit: 20,
        },
        queryDataModel: [{type: "default",label: "场次主题",prop: "title"},
          {type: "select",label: "状态",prop: "status",data: [
              {id: 0,name: "禁用"}, {id: 1,name: "启用"}]},
          {type: "datetime",label: "活动日期时间范围",prop: "dateTime",format: "yyyy-MM-dd HH:mm:ss"}],
        title: "",
        form: {
          status: 1,
        },
        dataList: [],
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.loading = true;
        getData(this.queryParams).then(response => {
          this.dataList = response.data.list;
          this.total = response.data.totalCount;
          this.loading = false;
        })
      },
      addData() {
        this.reset();
        this.title = "添加秒杀活动主题";
        this.$refs["dialog"].open();
      },
      amendData(row) {
        this.reset();
        this.form.id = row.id;
        info(row.id).then(response => {
          this.form = response.data;
          if (response.data.startTime && response.data.endTime) {
            this.form.dateTime = [response.data.startTime, response.data.endTime];
          }
          this.title = "修改秒杀活动主题";
          this.$refs["dialog"].open();
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        if (this.form.id != undefined) {
          update(this.form).then(response => {
            this.notSuccess("修改成功");
            this.$refs["dialog"].close();
            this.getList();
          });
        } else {
          save(this.form).then(response => {
            this.notSuccess("新增成功");
            this.$refs["dialog"].close();
            this.getList();
          });
        }
      },
      batchDelete() {
        if (this.ids && this.ids.length > 0) {
          let ids = this.ids;
          MessageBox.confirm('是否确认批量删除数据项？').then(function() {
            return delData(ids);
          }).then(() => {
            this.getList();
            this.notSuccess("删除成功");
          }).catch((err) => {
            console.log(err)
          });
        } else {
          this.notWarning("请选择需要删除的数据");
        }
      },
      deleteData(row) {
        MessageBox.confirm('是否确认删除名称为"' + row.name + '"的数据项？').then(function() {
          return delData(row.id);
        }).then(() => {
          this.getList();
          this.notSuccess("删除成功");
        }).catch(() => {});
      },
      handleSelectionChange(e) {
        let arr = []
        e.forEach(item => {
          arr.push(item.id);
        })
        this.ids = arr;
      },
      handleQuery(param) {
        this.getList();
      },
      resetQuery() {
        this.reset();
        this.getList();
      },
      reset() {
        this.title = "";
        this.form = {
          status: 1
        };
        this.queryParams = {
          page: 1,
          limit: 20,
        };
      }
    }
  }
</script>

<style scoped>
</style>
