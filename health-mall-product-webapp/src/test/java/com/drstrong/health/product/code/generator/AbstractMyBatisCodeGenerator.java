//package com.drstrong.health.product.code.generator;
//
//import cn.strong.mybatis.model.MyBatisPlusBaseModel;
//import cn.strong.mybatis.plus.extend.CustomBaseMapper;
//import cn.strong.mybatis.plus.extend.CustomServiceImpl;
//import com.baomidou.mybatisplus.core.toolkit.StringPool;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.po.TableInfo;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author <a href="mailto:wf2311@163.com">wf2311</a>
// * @since 2021/6/9 17:16.
// */
//public abstract class AbstractMyBatisCodeGenerator {
//    /**
//     * 数据源配置
//     */
//    protected DataSourceConfig dataSourceConfig = new DataSourceConfig();
//    /**
//     * 全局配置
//     */
//    protected GlobalConfig gc = new GlobalConfig();
//    protected String projectPath;
//
//    public void init() {
//        projectPath = System.getProperty("user.dir");
//        initDataSourceConfig();
//        initGlobalConfig();
//    }
//
//    protected void initGlobalConfig() {
//        gc.setOutputDir(projectPath + "/src/main/java");
//        gc.setFileOverride(true);
//        gc.setActiveRecord(true);
//        gc.setEnableCache(false);// XML 二级缓存
//        gc.setBaseResultMap(true);// XML ResultMap
//        gc.setBaseColumnList(true);// XML columList
//        gc.setOpen(false);
//        gc.setAuthor("mybatis plus generator");
//
//        // 自定义文件命名，注意 %s 会自动填充表实体属性！
//        gc.setMapperName("%sMapper");
//        gc.setXmlName("%sMapper");
//        gc.setServiceName("%sService");
//        gc.setServiceImplName("%sServiceImpl");
//        gc.setControllerName("%sController");
//    }
//
//    protected abstract void initDataSourceConfig();
//
//    protected void generate(PackageConfig pc, String[] tablePrefix, String[] tables) {
//        AutoGenerator mpg = new AutoGenerator();
//
//        StringBuilder xmlPath = new StringBuilder();
//        xmlPath.append("/src/main/java/");
//        xmlPath.append(pc.getParent().replaceAll("\\.", "/"));
//        xmlPath.append("/");
//        xmlPath.append(pc.getMapper().replaceAll("\\.", "/"));
//        xmlPath.append("/");
//
//        mpg.setGlobalConfig(gc);
//        mpg.setDataSource(dataSourceConfig);
//        mpg.setPackageInfo(pc);
//
//        // 自定义配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                // to do nothing
//            }
//        };
//        List<FileOutConfig> focList = new ArrayList<>();
//        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//
//                return projectPath + xmlPath
//                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//        mpg.setTemplate(new TemplateConfig().setXml(null));
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setSuperEntityClass(MyBatisPlusBaseModel.class.getName());
//        strategy.setSuperMapperClass(CustomBaseMapper.class.getName());
//        strategy.setSuperServiceImplClass(CustomServiceImpl.class.getName());
//        //此处可以修改为您的表前缀
//        strategy.setTablePrefix(tablePrefix);
//        // 表名生成策略
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        // 需要生成的表
//        strategy.setInclude(tables);
//        // 排除生成的表
//        //strategy.setExclude(new String[]{"test"});
//        strategy.setEntityLombokModel(true);
//
//        mpg.setStrategy(strategy);
//
//        // 执行生成
//        mpg.execute();
//    }
//}
