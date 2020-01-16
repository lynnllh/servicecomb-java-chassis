/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicecomb.codec.protobuf.definition;

import java.io.IOException;

import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.foundation.protobuf.RootDeserializer;
import org.apache.servicecomb.foundation.protobuf.internal.bean.PropertyWrapper;

import com.fasterxml.jackson.databind.JavaType;

public class ResponseRootDeserializer<T> {
  private RootDeserializer<T> rootDeserializer;

  private boolean empty;

  public ResponseRootDeserializer(RootDeserializer<T> rootDeserializer, boolean empty) {
    this.rootDeserializer = rootDeserializer;
    this.empty = empty;
  }

  @SuppressWarnings("unchecked")
  public T deserialize(byte[] bytes, JavaType invocationTimeType) throws IOException {
    if (empty) {
      rootDeserializer.deserialize(bytes); // read buffers if possible.
      return null;
    }

    Object obj = rootDeserializer.deserialize(bytes);
    if (obj instanceof PropertyWrapper) {
      obj = ((PropertyWrapper) obj).getValue();
    }
    if (obj != null && !invocationTimeType.isPrimitive() && !invocationTimeType.getRawClass()
        .isAssignableFrom(obj.getClass())) {
      obj = JsonUtils.convertValue(obj, invocationTimeType.getRawClass());
    }
    return (T) obj;
  }
}
