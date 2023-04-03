package org.itstack.demo.design.mediator;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公众号 | bugstack虫洞栈
 * 博 客 | https://bugstack.cn
 * Create by 小傅哥 @2020
 * 在这个类中包括的核⼼⽅法有； build(构建实例化元素) 、 parseConfiguration(解析配置) 、
 * dataSource(获取数据库配置) 、 connection(Map<String, String> dataSource) (链接数据
 * 库) 、 mapperElement (解析sql语句)
 */
public class SqlSessionFactoryBuilder {
    /**
     * 这个类主要⽤于创建解析xml⽂件的类，以及初始化SqlSession⼯⼚
     * 类 DefaultSqlSessionFactory 。另外需要注意这段代码 saxReader.setEntityResolver(new
     * XMLMapperEntityResolver()); ，是为了保证在不联⽹的时候⼀样可以解析xml，否则会需要从互联
     * ⽹获取dtd⽂件。
     * @param reader
     * @return
     */
    public DefaultSqlSessionFactory build(Reader reader) {
        SAXReader saxReader = new SAXReader();
        try {
            saxReader.setEntityResolver(new XMLMapperEntityResolver());
            Document document = saxReader.read(new InputSource(reader));
            Configuration configuration = parseConfiguration(document.getRootElement());
            return new DefaultSqlSessionFactory(configuration);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是对xml中的元素进⾏获取，这⾥主要获取了； dataSource 、 mappers ，⽽这两个配置⼀个是我们数
     * 据库的链接信息，另外⼀个是对数据库操作语句的解析
     * @param root
     * @return
     */
    private Configuration parseConfiguration(Element root) {
        Configuration configuration = new Configuration();
        configuration.setDataSource(dataSource(root.selectNodes("//dataSource")));
        configuration.setConnection(connection(configuration.dataSource));
        configuration.setMapperElement(mapperElement(root.selectNodes("mappers")));
        return configuration;
    }

    // 获取数据源配置信息
    private Map<String, String> dataSource(List<Element> list) {
        Map<String, String> dataSource = new HashMap<>(4);
        Element element = list.get(0);
        List content = element.content();
        for (Object o : content) {
            Element e = (Element) o;
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            dataSource.put(name, value);
        }
        return dataSource;
    }

    /**
     * 链接数据库的地⽅和我们常⻅的⽅式是⼀样的； Class.forName(dataSource.get("driver")); ，
     * 但是这样包装以后外部是不需要知道具体的操作。同时当我们需要链接多套数据库的时候，也是可以在
     * 这⾥扩展。
     * @param dataSource
     * @return
     */
    private Connection connection(Map<String, String> dataSource) {
        try {
            Class.forName(dataSource.get("driver"));
            return DriverManager.getConnection(dataSource.get("url"), dataSource.get("username"), dataSource.get("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 这部分代码块内容相对来说⽐较⻓，但是核⼼的点就是为了解析xml中的sql语句配置。在我们平常的使
     * ⽤中基本都会配置⼀些sql语句，也有⼀些⼊参的占位符。在这⾥我们使⽤正则表达式的⽅式进⾏解析操
     * 作。
     * 解析完成的sql语句就有了⼀个名称和sql的映射关系，当我们进⾏数据库操作的时候，这个组件就可以
     * 通过映射关系获取到对应sql语句进⾏操作。
     * @param list
     * @return
     */
    // 获取SQL语句信息
    private Map<String, XNode> mapperElement(List<Element> list) {
        Map<String, XNode> map = new HashMap<>();

        Element element = list.get(0);
        List content = element.content();
        for (Object o : content) {
            Element e = (Element) o;
            String resource = e.attributeValue("resource");

            try {
                Reader reader = Resources.getResourceAsReader(resource);
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new InputSource(reader));
                Element root = document.getRootElement();
                //命名空间
                String namespace = root.attributeValue("namespace");

                // SELECT
                List<Element> selectNodes = root.selectNodes("select");
                for (Element node : selectNodes) {
                    String id = node.attributeValue("id");
                    String parameterType = node.attributeValue("parameterType");
                    String resultType = node.attributeValue("resultType");
                    String sql = node.getText();

                    // ? 匹配
                    Map<Integer, String> parameter = new HashMap<>();
                    Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                    Matcher matcher = pattern.matcher(sql);
                    for (int i = 1; matcher.find(); i++) {
                        String g1 = matcher.group(1);
                        String g2 = matcher.group(2);
                        parameter.put(i, g2);
                        sql = sql.replace(g1, "?");
                    }

                    XNode xNode = new XNode();
                    xNode.setNamespace(namespace);
                    xNode.setId(id);
                    xNode.setParameterType(parameterType);
                    xNode.setResultType(resultType);
                    xNode.setSql(sql);
                    xNode.setParameter(parameter);

                    map.put(namespace + "." + id, xNode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return map;
    }

}
