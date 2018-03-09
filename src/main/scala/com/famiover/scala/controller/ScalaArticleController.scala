package com.famiover.scala.controller

import com.famiover.common.result.Result
import com.famiover.entity.article.Article
import com.famiover.entity.auth.User
import com.famiover.scala.service.ScalaArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(path = Array("/article"))
class ScalaArticleController(@Autowired private val scalaArticleService: ScalaArticleService) {

  /**
    * 根据文章ID查询文章
    *
    * @param id 文章ID
    */
  @RequestMapping(path = Array("/{id}"), method = Array(GET))
  def selectById(@PathVariable("id") id: java.lang.Long): Result[Article] = {
    val article = scalaArticleService.selectById(id)
    Result.success(article)



}
