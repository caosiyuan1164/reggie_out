package com.itheima;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;

public class Generator {
    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/reggie?" +
                "serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8" +
                "&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        generator.setDataSource(dataSource);

        //设置全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //设置输出路径
        globalConfig.setOutputDir("F:\\JAVADevelop\\projects\\reggie_take_out\\src\\main\\java");
        //设置生成完成后是否打开
        globalConfig.setOpen(false);
        //设置作者
        globalConfig.setAuthor("csy");
        //后生成的文件是否覆盖原来的文件
        globalConfig.setFileOverride(true);
        //设置接口名 %s为占位符，代指模块名
        globalConfig.setMapperName("%sDao");
        //设置Id生成策略
        globalConfig.setIdType(IdType.AUTO);

        //添加到生成器中
        generator.setGlobalConfig(globalConfig);

        //设置包名相关设置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.itheima");//设置包含的文件的包名
        packageConfig.setEntity("domain");//设置domain类的包名
        packageConfig.setMapper("dao");//设置dao的包名
        packageConfig.setController("controller");//设置controller的包名

        //添加到生成器中
        generator.setPackageInfo(packageConfig);

        //策略设置
        StrategyConfig strategyConfig = new StrategyConfig();
        //strategyConfig.setTablePrefix("mp_");//这样设置后，产生的类名前面就会去掉该前缀
        strategyConfig.setRestControllerStyle(true);//将生成的controller设置为rest风格
        //strategyConfig.setLogicDeleteFieldName("status");//设置逻辑删除字段
        //strategyConfig.setVersionFieldName("version");//设置乐观锁字段
        //strategyConfig.setInclude("setmeal");//设置包含的表，为可变参数，可设置多个
        //strategyConfig.setInclude("dish");
        strategyConfig.setInclude("orders");
        strategyConfig.setInclude("order_detail");

        generator.setStrategy(strategyConfig);


        generator.execute();
    }
}
