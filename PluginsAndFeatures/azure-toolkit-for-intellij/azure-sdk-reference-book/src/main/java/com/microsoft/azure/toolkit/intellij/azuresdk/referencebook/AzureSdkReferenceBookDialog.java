/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.azuresdk.referencebook;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AzureSdkReferenceBookDialog extends DialogWrapper {
    private final AzureSdkReferenceBookPanel bookPanel;

    protected AzureSdkReferenceBookDialog(@Nullable final Project project) {
        super(project);
        this.bookPanel = new AzureSdkReferenceBookPanel(project);
        this.setTitle("Azure SDK Reference Book");
        this.setModal(false);
        this.init();
    }

    public void selectFeature(@Nullable final String feature) {
        this.bookPanel.selectFeature(feature);
    }

    @Override
    @AzureOperation(name = "ui/sdk.open_sdk_reference_book", type = AzureOperation.Type.TASK, target = AzureOperation.Target.PLATFORM)
    public void show() {
        super.show();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.bookPanel.getContentPanel();
    }
}
