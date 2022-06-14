package com.microsoft.azure.toolkit.ide.guidance.input;

import com.microsoft.azure.toolkit.ide.guidance.Context;
import com.microsoft.azure.toolkit.ide.guidance.config.InputConfig;

import javax.annotation.Nonnull;

import static com.microsoft.azure.toolkit.ide.guidance.input.FileChooserInput.FILE_CHOOSER;

public class DefaultGuidanceInputProvider implements GuidanceInputProvider {
    @Override
    public GuidanceInput createInputComponent(@Nonnull InputConfig config, @Nonnull Context context) {
        final InputContext inputContext = new InputContext(config, context);
        final String name = config.getName();
        switch (name) {
            case FILE_CHOOSER:
                return new FileChooserInput(config, inputContext);
            default:
                return null;
        }
    }
}
