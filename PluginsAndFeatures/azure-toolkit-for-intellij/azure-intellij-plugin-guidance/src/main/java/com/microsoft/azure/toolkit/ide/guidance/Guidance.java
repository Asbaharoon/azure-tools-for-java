/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.ide.guidance;

import com.intellij.openapi.project.Project;
import com.microsoft.azure.toolkit.ide.guidance.config.SequenceConfig;
import com.microsoft.azure.toolkit.ide.guidance.phase.PhaseManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class Guidance {
    @Nonnull
    private final String id;
    @Nonnull
    private final String name;
    @Nonnull
    private final String title;
    @Nullable
    private final String description;
    @Nonnull
    private final String repository;
    @Nullable
    private final String uri;
    @Nonnull
    private final List<Phase> phases;
    @Nonnull
    private final Project project;
    @Nonnull
    private final Context context;
    @Nonnull
    private Status status = Status.INITIAL;

    private Phase currentPhase;

    public Guidance(@Nonnull final SequenceConfig sequenceConfig, @Nonnull Project project) {
        this.id = UUID.randomUUID().toString();
        this.project = project;
        this.name = sequenceConfig.getName();
        this.title = sequenceConfig.getTitle();
        this.description = sequenceConfig.getDescription();
        this.repository = sequenceConfig.getRepository();
        this.context = new Context(this, sequenceConfig.getContext());
        this.uri = sequenceConfig.getUri();
        this.phases = sequenceConfig.getPhases().stream().map(config -> PhaseManager.createPhase(config, this)).collect(Collectors.toList());
        this.initPhaseStatusListener();
    }

    public void init() {
        this.setStatus(Status.READY);
        if (CollectionUtils.isNotEmpty(phases)) {
            this.setCurrentPhase(phases.get(0));
            currentPhase.init();
        } else {
            this.setStatus(Status.SUCCEED);
        }
    }

    private void initPhaseStatusListener() {
        this.phases.forEach(step -> step.addStatusListener(status -> {
            if (status == Status.RUNNING || status == Status.FAILED) {
                this.setStatus(status);
            } else if (status == Status.SUCCEED) {
                this.setCurrentPhase(getNextPhase(step));
                if (currentPhase == null) {
                    this.setStatus(Status.SUCCEED);
                } else {
                    currentPhase.init();
                }
            }
        }));
    }

    @Nullable
    private Phase getNextPhase(Phase step) {
        for (int i = 0; i < phases.size() - 1; i++) {
            if (StringUtils.equals(step.getId(), phases.get(i).getId())) {
                return phases.get(i + 1);
            }
        }
        return null;
    }

    public void setCurrentPhase(final Phase phase) {
        final Phase oldPhase = this.currentPhase;
        this.currentPhase = phase;
        this.phaseListeners.forEach(listener -> listener.accept(oldPhase, phase));
    }

    private List<BiConsumer<Phase, Phase>> phaseListeners = new ArrayList<>();

    public void addPhaseListener(BiConsumer<Phase, Phase> listener) {
        phaseListeners.add(listener);
    }

    public void removePhaseListener(BiConsumer<Phase, Phase> listener) {
        phaseListeners.remove(listener);
    }
}
