package com.famiover.scala.service


import java.lang.reflect.Type
import java.util._

import com.famiover.common.page.Page
import com.famiover.common.utils.CollectionUtils
import com.famiover.entity.base.{BaseExample, BaseModel}
import com.famiover.mapper.base.BaseMapper
import com.famiover.scala.utils.MapperUtils
import org.springframework.beans.factory.annotation.Autowired
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl


object BaseService {
  private val ID = "Id"
}

abstract class BaseService[Model <: BaseModel[T], ModelExample <: BaseExample, T] extends IService {
  @Autowired private val baseMapper: BaseMapper[Model, ModelExample, T] = null
  /**
    * 根据主键查询
    */
  private val function = (id: T) => baseMapper.selectByPrimaryKey(id)


  /**
    * 查询数据库
    */
  def selectById(id: T): Model = {
    function.apply(id)
  }


  /**
    * 分页查询
    */
  protected def selectByPage(example: ModelExample): Page[Model] = {
    if (CollectionUtils.isNotEmpty(example.getColumns)) return selectByPageWithColumns(example)
    MapperUtils.selectByPage(example, baseMapper)
  }

  /**
    * 分页查询(自定义Columns)
    */
  private def selectByPageWithColumns(example: ModelExample) = MapperUtils.selectByPageWithColumns(example, baseMapper)

  /**
    * 根据Example查询
    */
  def selectByExample(example: ModelExample): java.util.List[Model] = {
    if (CollectionUtils.isNotEmpty(example.getColumns)) return selectByExampleWithColumns(example)
    MapperUtils.selectByExample(example, baseMapper)
  }

  /**
    * 根据Example查询(一条)
    */
  protected def selectOneByExample(example: ModelExample): Optional[Model] = MapperUtils.selectOneByExample(example, baseMapper)

  /**
    * 根据Example查询(自定义columns)
    */
  private def selectByExampleWithColumns(example: ModelExample) = MapperUtils.selectByExampleWithColumns(example, baseMapper)

  /**
    * 插入记录(对应字段有值)
    * ID主键不能赋值,自动生成
    */
  def insert(model: Model): Unit = {
    MapperUtils.insertSelective(model, baseMapper)
  }

  /**
    * 插入记录(对应字段有值)
    */
  def insertWithNowDate(model: Model): Unit = {
    val now = new Date
    model.setCreateDate(now)
    model.setEditDate(now)
    insert(model)
  }

  /**
    * 根据主键更新
    */
  protected def updateByPrimaryKeySelective(model: Model): Int = MapperUtils.updateByPrimaryKeySelective(model, baseMapper)

  protected def getModelType: Type = {
    val parameterType = this.getClass.getGenericSuperclass.asInstanceOf[ParameterizedTypeImpl]
    val types = parameterType.getActualTypeArguments
    types(0)
  }

  /**
    * 获取泛型中实体名称
    */

  override def getAppId: String = ""

  def getExample: ModelExample
}
