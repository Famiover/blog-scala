package com.famiover.scala.config

import javax.sql.DataSource

import com.alibaba.druid.filter.logging.Slf4jLogFilter
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder
import com.alibaba.druid.support.http.{StatViewServlet, WebStatFilter}
import com.google.common.collect.Lists
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.servlet.{FilterRegistrationBean, ServletRegistrationBean}
import org.springframework.context.annotation.{Bean, Configuration}


/**
  * Created by dongrj on 2016/9/5 14.
  * Email: famiover@163.cn
  */
@Configuration
class DruidConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.druid")
  def dataSource: DataSource = {
    val dataSource = DruidDataSourceBuilder.create.build
    dataSource.setProxyFilters(Lists.newArrayList(slf4jLogFilter))
    dataSource
  }

  @Bean
  def sqlSessionFactory(dataSource: DataSource): SqlSessionFactory = {
    val sqlSessionFactoryBean = new SqlSessionFactoryBean
    sqlSessionFactoryBean.setDataSource(dataSource)
    sqlSessionFactoryBean.getObject
  }

  @Bean
  def druidServlet: ServletRegistrationBean = {
    val servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet, "/druid/*")
    servletRegistrationBean.addInitParameter("allow", "127.0.0.1")
    servletRegistrationBean.addInitParameter("deny", "192.168.1.73")
    servletRegistrationBean.addInitParameter("loginUsername", "admin")
    servletRegistrationBean.addInitParameter("loginPassword", "famiover")
    servletRegistrationBean.addInitParameter("resetEnable", "true")
    servletRegistrationBean
  }

  @Bean
  def filterRegistrationBean: FilterRegistrationBean = {
    val filterRegistrationBean = new FilterRegistrationBean
    filterRegistrationBean.setFilter(new WebStatFilter)
    filterRegistrationBean.addUrlPatterns("/druid/*")
    filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*")
    filterRegistrationBean
  }

  @Bean
  def slf4jLogFilter: Slf4jLogFilter = {
    val slf4jLogFilter = new Slf4jLogFilter
    slf4jLogFilter.setStatementLogErrorEnabled(true)
    slf4jLogFilter.setStatementExecutableSqlLogEnable(true)
    slf4jLogFilter.setStatementParameterSetLogEnabled(true)
    slf4jLogFilter.setStatementParameterClearLogEnable(false)
    slf4jLogFilter.setStatementSqlPrettyFormat(true)
    slf4jLogFilter
  }
}

