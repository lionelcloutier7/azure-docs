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

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.EventHubTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

/**
 * @author Soby Chacko
 */
public class FooHandler {

	@FunctionName("Update-Inventory")
	public void update(
			@EventHubTrigger(name = "data", eventHubName = "events", connection = "TRANSACTIONS_EVENT_HUB_CONNECTION_STRING") //
			String data,
			@CosmosDBOutput(name = "document", databaseName = "inventory", collectionName = "messages", //
					connectionStringSetting = "PRODUCT_ITEMS_DOCUMENTDB_CONNECTION_STRING", createIfNotExists = true) //
			OutputBinding<String> document, final ExecutionContext context) {
		context.getLogger()
				.info("Java Event Hub transaction trigger processed a request: " + data);
		document.setValue(data);
		context.getLogger().info("Response: " + document.getValue());
	}
}
