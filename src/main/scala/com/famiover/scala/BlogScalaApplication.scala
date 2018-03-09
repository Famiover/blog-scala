package com.famiover.scala

import com.famiover.mapper.base.Mapper
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = Array("com.famiover"))
@MapperScan(basePackages = Array("com.famiover.mapper"), annotationClass = classOf[Mapper])
class BlogScalaApplication

object BlogScalaApplication extends App {
  SpringApplication.run(classOf[BlogScalaApplication])
}