package io.react2.reactdynamo

trait Format extends DefaultReads with DefaultWrites {

  def value[T: Writes](t: T): AttrValue = implicitly[Writes[T]].writes(t)
  
  def attr[T: Writes](t: T): AttrValue = implicitly[Writes[T]].writes(t)
  
  implicit def attrValue2Rich(attr: AttrValue): RichAttrValue = new RichAttrValue(attr)

}