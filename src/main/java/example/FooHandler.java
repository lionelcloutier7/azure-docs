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

import java.util.UUID;

import com.microsoft.azure.serverless.functions.ExecutionContext;
import com.microsoft.azure.serverless.functions.OutputBinding;
import com.microsoft.azure.serverless.functions.annotation.DocumentDBOutput;
import com.microsoft.azure.serverless.functions.annotation.EventHubTrigger;
import com.microsoft.azure.serverless.functions.annotation.FunctionName;

import org.json.JSONObject;

/**
 * @author Soby Chacko
 */
public class FooHandler {

	@FunctionName("Update-Inventory")
	public void update(
			@EventHubTrigger(name = "data", eventHubName = "inventoryeh", connection = "InventoryEventHubTransactionsConnectionString") String data,
			@DocumentDBOutput(name = "document", databaseName = "inventory", collectionName = "transactions", connection = "InventoryCosmosDBConnectionString", createIfNotExists = true) OutputBinding<String> document,
			final ExecutionContext context) {

		context.getLogger()
				.info("Java Event Hub transaction trigger processed a request: " + data);
		JSONObject eventGridMessage = new JSONObject(data);
		eventGridMessage.put("id", UUID.randomUUID().toString());
		context.getLogger().info("message: " + eventGridMessage.toString());
		document.setValue(eventGridMessage.toString());
	}
}
