/*
 * Copyright 2017 the original author or authors.
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

package example;

import java.util.function.Function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.EventHubTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;
import org.springframework.context.annotation.Bean;

/**
 * @author Soby Chacko
 */
@SpringBootApplication
public class FooHandler extends AzureSpringBootRequestHandler<Foo, Bar> {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(FooHandler.class, args);
	}

	@Bean
	public Function<Foo, Bar> uppercase() {
		return foo -> new Bar(foo.getValue().toUpperCase());
	}

	@FunctionName("Foo-Bar")
	public void update(
			@EventHubTrigger(name = "data", eventHubName = "events", connection = "TRANSACTIONS_EVENT_HUB_CONNECTION_STRING") //
			Foo data,
			@CosmosDBOutput(name = "document", databaseName = "inventory", collectionName = "messages", //
					connectionStringSetting = "PRODUCT_ITEMS_DOCUMENTDB_CONNECTION_STRING", createIfNotExists = true) //
			OutputBinding<Bar> document, final ExecutionContext context) {
		handleOutput(data, document, context);
	}
}

class Foo {

	private String value;

	Foo() {
	}

	public String lowercase() {
		return value.toLowerCase();
	}

	public Foo(String value) {
		this.value = value;
	}

	public String uppercase() {
		return value.toUpperCase();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

class Bar {

	private String value;

	Bar() {
	}

	public Bar(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}