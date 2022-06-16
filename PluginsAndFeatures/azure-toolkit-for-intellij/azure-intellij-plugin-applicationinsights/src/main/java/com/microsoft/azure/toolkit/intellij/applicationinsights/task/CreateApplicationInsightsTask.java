package com.microsoft.azure.toolkit.intellij.applicationinsights.task;

import com.microsoft.azure.toolkit.ide.guidance.ComponentContext;
import com.microsoft.azure.toolkit.ide.guidance.GuidanceTask;
import com.microsoft.azure.toolkit.lib.Azure;
import com.microsoft.azure.toolkit.lib.applicationinsights.ApplicationInsight;
import com.microsoft.azure.toolkit.lib.applicationinsights.ApplicationInsightDraft;
import com.microsoft.azure.toolkit.lib.applicationinsights.AzureApplicationInsights;
import com.microsoft.azure.toolkit.lib.auth.AzureAccount;
import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azure.toolkit.lib.common.model.Region;
import com.microsoft.azure.toolkit.lib.common.model.Subscription;
import com.microsoft.azure.toolkit.lib.common.utils.Utils;
import com.microsoft.azure.toolkit.lib.resource.AzureResources;
import com.microsoft.azure.toolkit.lib.resource.ResourceGroup;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CreateApplicationInsightsTask implements GuidanceTask {

    public static final String APPLICATION_INSIGHTS_NAME = "applicationInsightsName";
    public static final String SUBSCRIPTION_ID = "subscriptionId";
    public static final String RESOURCE_GROUP = "resourceGroup";
    public static final String RESOURCE_ID = "resourceId";
    public static final String DEFAULT_APPLICATION_INSIGHTS_NAME = "defaultApplicationInsightsName";
    private final ComponentContext context;

    public CreateApplicationInsightsTask(@Nonnull final ComponentContext context) {
        this.context = context;
    }

    @Override
    public void execute() throws Exception {
        final String name = (String) context.getParameter(APPLICATION_INSIGHTS_NAME);
        final String subscriptionId = (String) context.getParameter(SUBSCRIPTION_ID);
        final String resourceGroupName = (String) context.getParameter(RESOURCE_GROUP);
        final Subscription subscription = Optional.ofNullable(subscriptionId)
                .map(id -> Azure.az(AzureAccount.class).account().getSubscription(id))
                .orElseGet(() -> Azure.az(AzureAccount.class).getSubscriptions().get(0));
        final ResourceGroup resourceGroup = Optional.ofNullable(resourceGroupName)
                .map(rg -> Azure.az(AzureResources.class).groups(subscription.getId()).get(rg, rg))
                .orElseThrow(() -> new AzureToolkitRuntimeException("Failed to get resource group to create application insight"));
        final Region region = resourceGroup.getRegion();
        final ApplicationInsightDraft applicationInsightDraft = Azure.az(AzureApplicationInsights.class).applicationInsights(subscription.getId())
                .create(name, resourceGroupName);
        applicationInsightDraft.setRegion(region);
        final ApplicationInsight result = applicationInsightDraft.commit();
        context.applyResult(RESOURCE_ID, result.getId());
    }

    @Override
    public void init() {
        GuidanceTask.super.init();
        final String defaultApplicationInsightsName =
                String.format("ai-%s-%s", context.getGuidance().getName(), Utils.getTimestamp());
        context.applyResult(DEFAULT_APPLICATION_INSIGHTS_NAME, defaultApplicationInsightsName);
    }

}
