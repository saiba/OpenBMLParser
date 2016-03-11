/*******************************************************************************
 * Copyright (C) 2009 Human Media Interaction, University of Twente, the Netherlands
 * 
 * This file is part of the Elckerlyc BML realizer.
 * 
 * Elckerlyc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Elckerlyc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Elckerlyc.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
package saiba.bml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import saiba.bml.core.Behaviour;
import saiba.bml.core.FaceLexemeBehaviour;
import saiba.bml.core.GazeBehaviour;
import saiba.bml.core.GazeShiftBehaviour;
import saiba.bml.core.GestureBehaviour;
import saiba.bml.core.HeadBehaviour;
import saiba.bml.core.HeadDirectionShiftBehaviour;
import saiba.bml.core.LocomotionBehaviour;
import saiba.bml.core.PointingBehaviour;
import saiba.bml.core.PostureBehaviour;
import saiba.bml.core.PostureShiftBehaviour;
import saiba.bml.core.SpeechBehaviour;
import saiba.bml.core.WaitBehaviour;
import saiba.bml.core.ext.FaceFacsBehaviour;
import saiba.bml.feedback.BMLFeedback;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.SetMultimap;

/**
 * Singleton for BML parsing setup, refactor if we ever want to use multiple parsers
 * 
 * @author welberge
 */
public final class BMLInfo
{

    private static Set<String> externalBlockIds = new HashSet<String>();

    public static final String BMLNAMESPACE = "http://www.bml-initiative.org/bml/bml-1.0";
    public static final String BMLEXTNAMESPACE = "http://www.bml-initiative.org/bml/coreextensions-1.0";
    
    public static String behTag(String tag)
    {
        return BMLNAMESPACE+":"+tag; 
    }
    
    public static String behExtTag(String tag)
    {
        return BMLEXTNAMESPACE+":"+tag; 
    }
    
    // /Behaviors that are parsed
    private static final ImmutableMap<String, Class<? extends Behaviour>> BEHAVIOR_TYPES = 
//@formatter:off
          new ImmutableMap.Builder<String, Class<? extends Behaviour>>()
            .put(behTag(HeadBehaviour.xmlTag()), HeadBehaviour.class)
            .put(behTag(HeadDirectionShiftBehaviour.xmlTag()), HeadDirectionShiftBehaviour.class)
            .put(behTag(LocomotionBehaviour.xmlTag()), LocomotionBehaviour.class)
            .put(behTag(FaceLexemeBehaviour.xmlTag()), FaceLexemeBehaviour.class)
            .put(behTag(GazeShiftBehaviour.xmlTag()), GazeShiftBehaviour.class)
            .put(behTag(GazeBehaviour.xmlTag()), GazeBehaviour.class)
            .put(behTag(PostureBehaviour.xmlTag()), PostureBehaviour.class)
            .put(behTag(GestureBehaviour.xmlTag()), GestureBehaviour.class)
            .put(behTag(SpeechBehaviour.xmlTag()), SpeechBehaviour.class)
            .put(behTag(WaitBehaviour.xmlTag()), WaitBehaviour.class)
            .put(behTag(PointingBehaviour.xmlTag()), PointingBehaviour.class)
            .put(behExtTag(FaceFacsBehaviour.xmlTag()), FaceFacsBehaviour.class)
            .put(behTag(PostureShiftBehaviour.xmlTag()), PostureShiftBehaviour.class)
            .build();
        //@formatter:on
    // /Description levels that can be parsed
    private static final ImmutableMap<String, Class<? extends Behaviour>> DESCRIPTION_EXTENSIONS = new ImmutableMap.Builder<String, Class<? extends Behaviour>>()
            .build();

    // /supported extensions in the scheduler. Will be filled by scheduler.
    public static final List<Class<? extends Behaviour>> supportedExtensions = new ArrayList<Class<? extends Behaviour>>();

    private static Map<String, Class<? extends Behaviour>> behaviourTypes = new HashMap<String, Class<? extends Behaviour>>();
    private static Map<String, Class<? extends Behaviour>> descriptionExtensions = new HashMap<String, Class<? extends Behaviour>>();
    private static SetMultimap<Class<? extends Behaviour>, String> customStringAttributes = HashMultimap.create();
    private static SetMultimap<Class<? extends Behaviour>, String> customFloatAttributes = HashMultimap.create();
    private static SetMultimap<Class<? extends BMLFeedback>, String> customFeedbackStringAttributes = HashMultimap.create();
    private static SetMultimap<Class<? extends BMLFeedback>, String> customFeedbackFloatAttributes = HashMultimap.create();

    static
    {
        behaviourTypes.putAll(BEHAVIOR_TYPES);
        descriptionExtensions.putAll(DESCRIPTION_EXTENSIONS);
    }

    /**
     * These ids might also be used instead of BML ids. Used in Elckerlyc/Asap for anticipators:<syncid>
     */
    public static void addExternalBlockId(String id)
    {
        externalBlockIds.add(id);
    }

    public static boolean isExternalBlockId(String id)
    {
        return externalBlockIds.contains(id);
    }

    public static void addCustomStringAttribute(Class<? extends Behaviour> beh, String namespace, String attributeName)
    {
        customStringAttributes.put(beh, namespace + ":" + attributeName);
    }

    public static void addCustomFeedbackStringAttribute(Class<? extends BMLFeedback> beh, String namespace, String attributeName)
    {
        customFeedbackStringAttributes.put(beh, namespace + ":" + attributeName);
    }

    public static void addCustomStringAttribute(Class<? extends Behaviour> beh, String namespace, List<String> attributeNames)
    {
        for (String att : attributeNames)
        {
            addCustomStringAttribute(beh, namespace, att);
        }
    }

    public static void addCustomFloatAttribute(Class<? extends Behaviour> beh, String namespace, String attributeName)
    {
        customFloatAttributes.put(beh, namespace + ":" + attributeName);
    }

    public static void addCustomFeedbackFloatAttribute(Class<? extends BMLFeedback> beh, String namespace, String attributeName)
    {
        customFeedbackFloatAttributes.put(beh, namespace + ":" + attributeName);
    }

    public static void addCustomFloatAttribute(Class<? extends Behaviour> beh, String namespace, List<String> attributeNames)
    {
        for (String att : attributeNames)
        {
            addCustomFloatAttribute(beh, namespace, att);
        }
    }

    public static Set<String> getCustomFloatAttributes(Class<? extends Behaviour> beh)
    {
        return customFloatAttributes.get(beh);
    }

    public static Set<String> getCustomStringAttributes(Class<? extends Behaviour> beh)
    {
        return customStringAttributes.get(beh);
    }

    public static Set<String> getCustomFeedbackFloatAttributes(Class<? extends BMLFeedback> beh)
    {
        return customFeedbackFloatAttributes.get(beh);
    }

    public static Set<String> getCustomFeedbackStringAttributes(Class<? extends BMLFeedback> beh)
    {
        return customFeedbackStringAttributes.get(beh);
    }

    public static void addBehaviourTypes(ImmutableMap<String, Class<? extends Behaviour>> behs)
    {
        behaviourTypes.putAll(behs);
    }

    public static void addDescriptionExtensions(ImmutableMap<String, Class<? extends Behaviour>> descs)
    {
        descriptionExtensions.putAll(descs);
    }

    public static void addBehaviourType(String name, Class<? extends Behaviour> beh)
    {
        behaviourTypes.put(name, beh);
    }

    public static void addDescriptionExtension(String name, Class<? extends Behaviour> beh)
    {
        descriptionExtensions.put(name, beh);
    }

    public static Map<String, Class<? extends Behaviour>> getBehaviourTypes()
    {
        return behaviourTypes;
    }

    public static Map<String, Class<? extends Behaviour>> getDescriptionExtensions()
    {
        return descriptionExtensions;
    }

    /**
     * Constructor, adds the default BML behaviors and parseable extensions to the behaviourTypes HashMap.
     */
    private BMLInfo()
    {

    }
}
