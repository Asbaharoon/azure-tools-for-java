/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.hdinsight.sdk.rest.azure.datalake.analytics.accounts.models;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.rest.ExpandableStringEnum;

/**
 * Defines values for OperationOrigin.
 */
public final class OperationOrigin extends ExpandableStringEnum<OperationOrigin> {
    /** Static value user for OperationOrigin. */
    public static final OperationOrigin USER = fromString("user");

    /** Static value system for OperationOrigin. */
    public static final OperationOrigin SYSTEM = fromString("system");

    /** Static value user,system for OperationOrigin. */
    public static final OperationOrigin USERSYSTEM = fromString("user,system");

    /**
     * Creates or finds a OperationOrigin from its string representation.
     * @param name a name to look for
     * @return the corresponding OperationOrigin
     */
    @JsonCreator
    public static OperationOrigin fromString(String name) {
        return fromString(name, OperationOrigin.class);
    }

    /**
     * @return known OperationOrigin values
     */
    public static Collection<OperationOrigin> values() {
        return values(OperationOrigin.class);
    }
}
