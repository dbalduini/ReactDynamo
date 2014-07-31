package io.react2.reactdynamo

trait Format extends DefaultReads with DefaultWrites {

  def read[T: Reads](a: AttributeValue): T = implicitly[Reads[T]].reads(a)

  def write[T: Writes](t: T): AttributeValue = implicitly[Writes[T]].writes(t)

  def key(k: String) = new KeyValue(k)

}

private[reactdynamo] case class KeyValue(key: String) {

  type Pair = Option[(String, AttributeValue)]

  def value[T: Writes](t: T): Pair = Some(key -> implicitly[Writes[T]].writes(t))

  //TODO TEMPORARY METHOOD POR OPTIONAL WRITES
  def valueOpt[T: Writes](opt: Option[T])(implicit w: Writes[T]): Pair = opt match {
    case Some(t) => value(t)(w)
    case None => None
  }

  def update[T: Writes](value: T, action: AttributeAction): ItemUpdate =
    Map(key -> new AttributeValueUpdate(implicitly[Writes[T]].writes(value), action))

}
