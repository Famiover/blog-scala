package com.famiover.scala.utils

import java.util
import java.util.{Date, Objects, Optional}

import com.famiover.common.page.Page
import com.famiover.common.utils.{CollectionUtils, PreconditionsUtils}
import com.famiover.entity.base.{BaseExample, BaseModel, ExampleUtils}
import com.famiover.mapper.base.BaseMapper
import com.google.common.collect.Lists


object MapperUtils {

  def selectByPage[PK, Model <: BaseModel[PK], Example <: BaseExample](baseExample: Example, baseMapper: BaseMapper[Model, Example, PK]): Page[Model] = {
    ExampleUtils.setDefaultCurrentPageAndPageSize(baseExample)
    val biFunction = (example: Example, mapper: BaseMapper[Model, Example, PK]) => {
      def page(example: Example, mapper: BaseMapper[Model, Example, PK]): Page[Model] = {
        val count = mapper.countByExample(example)
        if (count == 0) {
          return getEmptyPage(example)
        }
        val list = mapper.selectByExample(example)
        val page = new Page[Model]
        page.setCurrentPage(example.getCurrentPage)
        page.setPageSize(example.getPageSize)
        page.setData(list)
        page.setTotalRow(count)
        page
      }
      page(example, mapper)
    }
    biFunction.apply(baseExample, baseMapper)
  }


  def selectByPageWithColumns[PK, Model <: BaseModel[PK], Example <: BaseExample](baseExample: Example, baseMapper: BaseMapper[Model, Example, PK]): Page[Model] = {
    val biFunction = (example: Example, mapper: BaseMapper[Model, Example, PK]) => {
      def page(example: Example, mapper: BaseMapper[Model, Example, PK]): Page[Model] = {
        val count = mapper.countByExample(example)
        if (count == 0) {
          return getEmptyPage(example)
        }
        val list = mapper.selectByExampleSelective(example)
        val page = new Page[Model]
        page.setCurrentPage(example.getCurrentPage)
        page.setPageSize(example.getPageSize)
        page.setData(list)
        page.setTotalRow(count)
        page
      }
      page(example, mapper)
    }
    biFunction.apply(baseExample, baseMapper)
  }


  private def getEmptyPage[Model, Example <: BaseExample](example: Example) = {
    val page = new Page[Model]
    page.setPageSize(example.getPageSize)
    page.setCurrentPage(example.getCurrentPage)
    page.setData(Lists.newArrayList())
    page
  }


  def insertSelective[PK, Model <: BaseModel[PK], Example <: BaseExample](model: Model, baseMapper: BaseMapper[Model, Example, PK]): Unit = {
    val date = new Date
    model.setCreateDate(date)
    model.setEditDate(date)
    val biConsumer = (baseModel: Model, mapper: BaseMapper[Model, Example, PK]) => mapper.insertSelective(baseModel)
    biConsumer(model, baseMapper)
  }

  def selectByExample[PK, Model <: BaseModel[PK], Example <: BaseExample](baseExample: Example, baseMapper: BaseMapper[Model, Example, PK]): util.List[Model] = {
    PreconditionsUtils.checkArgument(Objects.isNull(baseExample.getStart) || baseExample.getStart < 0, "Start必须为空!")
    val biFunction = (example: Example, mapper: BaseMapper[Model, Example, PK]) => mapper.selectByExample(example)
    biFunction.apply(baseExample, baseMapper)
  }

  def selectByExampleWithColumns[PK, Model <: BaseModel[PK], Example <: BaseExample](baseExample: Example, baseMapper: BaseMapper[Model, Example, PK]): util.List[Model] = {
    PreconditionsUtils.checkArgument(Objects.isNull(baseExample.getStart) || baseExample.getStart < 0, "Start必须为空!")
    PreconditionsUtils.checkArgument(CollectionUtils.size(baseExample.getColumns) > 0, "Columns必须有值!")
    val biFunction = (example: Example, mapper: BaseMapper[Model, Example, PK]) => mapper.selectByExampleSelective(example)
    biFunction.apply(baseExample, baseMapper)
  }

  def selectOneByExample[PK, Model <: BaseModel[PK], Example <: BaseExample](baseExample: Example, baseMapper: BaseMapper[Model, Example, PK]): Optional[Model] = {
    baseExample.setCurrentPage(1)
    baseExample.setPageSize(1)
    val models = selectByExample(baseExample, baseMapper)
    CollectionUtils.first(models)
  }

  def updateByPrimaryKeySelective[PK, Model <: BaseModel[PK], Example <: BaseExample](model: Model, baseMapper: BaseMapper[Model, Example, PK]): Int = {
    PreconditionsUtils.checkArgument(Objects.nonNull(model.getId), "主键ID不能为空!")
    baseMapper.updateByPrimaryKeySelective(model)
  }
}

