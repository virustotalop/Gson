/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.pixelverse.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.InstanceCreator;
import com.google.gson.LongSerializationPolicy;
import net.pixelverse.gson.internal.$Gson$Preconditions;
import net.pixelverse.gson.internal.Excluder;
import net.pixelverse.gson.internal.bind.TreeTypeAdapter;
import net.pixelverse.gson.internal.bind.TypeAdapters;
import net.pixelverse.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.pixelverse.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.*;

/**
 * <p>Use this builder to construct a {@link Gson} instance when you need to set configuration
 * options other than the default. For {@link Gson} with default configuration, it is simpler to
 * use {@code new Gson()}. {@code GsonBuilder} is best used by creating it, and then invoking its
 * various configuration methods, and finally calling create.</p>
 *
 * <p>The following is an example shows how to use the {@code GsonBuilder} to construct a Gson
 * instance:
 *
 * <pre>
 * Gson gson = new GsonBuilder()
 *     .registerTypeAdapter(Id.class, new IdTypeAdapter())
 *     .enableComplexMapKeySerialization()
 *     .serializeNulls()
 *     .setDateFormat(DateFormat.LONG)
 *     .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
 *     .setPrettyPrinting()
 *     .setVersion(1.0)
 *     .create();
 * </pre></p>
 *
 * <p>NOTES:
 * <ul>
 * <li> the order of invocation of configuration methods does not matter.</li>
 * <li> The default serialization of {@link Date} and its subclasses in Gson does
 *  not contain time-zone information. So, if you are using date/time instances,
 *  use {@code GsonBuilder} and its {@code setDateFormat} methods.</li>
 *  </ul>
 * </p>
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 * @author Jesse Wilson
 */
public final class SuperGsonBuilder extends GsonBuilder {

  @Override
  public SuperGsonBuilder setPrettyPrinting() {
    return (SuperGsonBuilder) super.setPrettyPrinting();
  }

  @Override
  public SuperGsonBuilder disableInnerClassSerialization() {
    return (SuperGsonBuilder) super.disableInnerClassSerialization();
  }

  /**
   * Creates a {@link Gson} instance based on the current configuration. This method is free of
   * side-effects to this {@code GsonBuilder} instance and hence can be called multiple times.
   *
   * @return an instance of Gson configured with the options currently set in this builder
   */
  public SuperGson create() {
    List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>(this.factories.size() + this.hierarchyFactories.size() + 3);
    factories.addAll(this.factories);
    Collections.reverse(factories);

    List<TypeAdapterFactory> hierarchyFactories = new ArrayList<TypeAdapterFactory>(this.hierarchyFactories);
    Collections.reverse(hierarchyFactories);
    factories.addAll(hierarchyFactories);

    addTypeAdaptersForDate(datePattern, dateStyle, timeStyle, factories);

    return new SuperGson(excluder, fieldNamingPolicy, instanceCreators,
        serializeNulls, complexMapKeySerialization,
        generateNonExecutableJson, escapeHtmlChars, prettyPrinting, lenient,
        serializeSpecialFloatingPointValues, longSerializationPolicy,
        datePattern, dateStyle, timeStyle,
        this.factories, this.hierarchyFactories, factories);
  }
}
