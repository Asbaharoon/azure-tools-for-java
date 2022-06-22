package com.microsoft.azure.toolkit.ide.guidance.view.components;

import com.intellij.ui.HyperlinkLabel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.JBUI;
import com.microsoft.azure.toolkit.ide.guidance.Status;
import com.microsoft.azure.toolkit.ide.guidance.Step;
import com.microsoft.azure.toolkit.ide.guidance.input.GuidanceInput;
import com.microsoft.azure.toolkit.lib.common.bundle.AzureString;
import com.microsoft.azure.toolkit.lib.common.form.AzureValidationInfo;
import com.microsoft.azure.toolkit.lib.common.messager.IAzureMessage;
import com.microsoft.azure.toolkit.lib.common.messager.IAzureMessager;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.Optional;

public class StepPanel extends JPanel {
    private JPanel contentPanel;
    private JLabel statusIcon;
    private JLabel titleLabel;
    private JTextPane descPanel;
    private JTextArea outputPanel;
    private HyperlinkLabel actionButton;

    private final Step step;

    public StepPanel(@Nonnull final Step step) {
        super();
        this.step = step;
        $$$setupUI$$$();
        init();
    }

    private void init() {
        this.setLayout(new GridLayoutManager(1, 1));
        this.add(this.contentPanel, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, null, null, null, 0));
        this.step.addStatusListener(this::updateStatus);
        this.actionButton.setHyperlinkText("Run");
        this.actionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.actionButton.setHyperlinkTarget(null);
        this.actionButton.addHyperlinkListener(e -> {
            this.descPanel.setVisible(false);
            final AzureString title = AzureString.format("run step '%s'", this.step.getTitle());
            AzureTaskManager.getInstance().runInBackground(title, this.step::execute);
        });
        this.descPanel.setBorder(null);
        this.descPanel.setVisible(StringUtils.isNotBlank(this.step.getDescription()));
        this.renderDescription();
        this.initOutputPanel();
        this.updateStatus(this.step.getStatus());
        this.step.getContext().addContextListener(ignore -> this.renderDescription());
    }

    private void renderDescription() {
        titleLabel.setText(step.getRenderedTitle());
        descPanel.setText(step.getRenderedDescription());
    }

    private void initOutputPanel() {
        final IAzureMessager messager = new ConsoleTextMessager();
        this.step.setOutput(messager);
        this.outputPanel.setVisible(false);
        final CompoundBorder border = BorderFactory.createCompoundBorder(this.outputPanel.getBorder(), BorderFactory.createEmptyBorder(2, 4, 4, 4));
        this.outputPanel.setBorder(border);
        this.outputPanel.setBackground(JBUI.CurrentTheme.EditorTabs.background());
    }

    class ConsoleTextMessager implements IAzureMessager {
        @Override
        public boolean show(IAzureMessage message) {
            Optional.ofNullable(StepPanel.this.step.getPhase().getOutput()).ifPresent(messager -> messager.show(message)); // Also write to step output
            return true;
        }
    }

    private void updateStatus(Status status) {
        final Icon icon = IconUtil.scale(PhasePanel.getStatusIcon(status), this.statusIcon, 0.875f);
        this.statusIcon.setIcon(icon);
        this.actionButton.setVisible(status != Status.SUCCEED);
        this.actionButton.setEnabled(status == Status.READY || status == Status.FAILED);
        if (status == Status.FAILED) {
            this.actionButton.setHyperlinkText("Retry");
        }
    }

    void $$$setupUI$$$() {
    }
}
