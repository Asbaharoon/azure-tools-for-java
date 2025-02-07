/*
 * Copyright (c) Microsoft Corporation
 *
 * All rights reserved.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.microsoft.azure.hdinsight.spark.ui

import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.microsoft.azure.cosmosspark.common.JXHyperLinkWithUri
import com.microsoft.azure.hdinsight.sdk.common.AzureSparkClusterManager
import com.microsoft.intellij.actions.AzureSignInAction
import com.microsoft.intellij.forms.dsl.panel
import javax.swing.JLabel
import javax.swing.JPanel

class SparkSubmissionJobUploadStorageAdlsSignOutCard: JPanel() {
    val title: String = "Sign Out"
    val azureAccountLabel = JLabel().apply {
        text = AzureSparkClusterManager.getInstance().azureAccountEmail
    }
    val signOutLink = JXHyperLinkWithUri().apply {
        name = "adlsCardSignOutHyperLink"
        text = "Sign Out"
        addActionListener { AzureSignInAction.authActionPerformed(null) }
    }

    init {
        val formBuilder = panel {
            columnTemplate {
                col {
                    anchor = ANCHOR_WEST
                }
                col {
                    anchor = ANCHOR_WEST
                }
                row {
                    c(azureAccountLabel); c(signOutLink)
                }
            }
        }

        layout = formBuilder.createGridLayoutManager()
        formBuilder.allComponentConstraints.forEach { (component, gridConstrains) -> add(component, gridConstrains) }
    }
}
