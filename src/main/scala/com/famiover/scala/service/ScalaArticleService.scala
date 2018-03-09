package com.famiover.scala.service

import java.lang.Long

import com.famiover.entity.article.{Article, ArticleExample}
import org.springframework.stereotype.Service

@Service
class ScalaArticleService extends BaseService[Article, ArticleExample, Long] {

  /**
    * 根据文章ID删除文章
    *
    * @param id 文章ID
    */
  def deleteById(id: java.lang.Long): Unit = {
    val Article = new Article()
    Article.setId(id)
    Article.setStatus(1)
    updateByPrimaryKeySelective(Article)
  }


  /**
    * 当前Example
    */
  override def getExample: ArticleExample = new ArticleExample


  override def getAppId: String = {
    ""
  }
}
