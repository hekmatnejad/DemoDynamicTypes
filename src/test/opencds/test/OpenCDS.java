package opencds.test;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.factmodel.traits.TraitFactory;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.opencds.vmr.v1_0.internal.*;
import org.opencds.vmr.v1_0.internal.datatypes.CD;

import static junit.framework.Assert.fail;

/**
 * Created by mamad on 5/1/14.
 */
public class OpenCDS {

    @Test
    public void testTraitOpencds() {


        KnowledgeBase kBase = buildKB("opencds/test/opencdsRule.drl");
        StatefulKnowledgeSession ksession = kBase.newStatefulKnowledgeSession();
        TraitFactory.setMode(TraitFactory.VirtualPropertyMode.MAP, ksession.getKnowledgeBase());

        //Influenza facts initialization
        ObservationResult observationResult = new ObservationResult();
        observationResult.setId("");
        CD cdFocus = new CD();
        cdFocus.setCodeSystem("AHRQ v4.3");
        cdFocus.setCode("C261");
        observationResult.setObservationFocus(cdFocus);

        CD cdCoded = new CD();
        cdCoded.setCodeSystem("AHRQ v4.3");
        cdCoded.setCode("C87");
        ObservationValue obsValue = new ObservationValue();
        obsValue.setConcept(cdCoded);
        obsValue.setIdentifier("");
        observationResult.setObservationValue(obsValue);

        ksession.insert(observationResult);
        ksession.insert(obsValue);

        //AcuteRespiratory facts initialization
        EncounterEvent encounterEvent = new EncounterEvent();
        ClinicalStatementRelationship clinicalStatementRelationship = new ClinicalStatementRelationship();
        Problem problem = new Problem();
        CD cdEncounterType = new CD();
        cdEncounterType.setCodeSystem("AHRQ v4.3");
        cdEncounterType.setCode("C238");
        encounterEvent.setId("C238");
        encounterEvent.setEncounterType(cdEncounterType);
        CD cdImportance = new CD();
        cdImportance.setCodeSystem("AHRQ v4.3");
        cdImportance.setCode("C417");
        problem.setImportance(cdImportance);
        CD cdProblemCode = new CD();
        cdProblemCode.setCodeSystem("AHRQ v4.3");
        cdProblemCode.setCode("C284");
        problem.setId("C284");
        problem.setProblemCode(cdProblemCode);
        CD cdTargetRelRoSource = new CD();
        cdTargetRelRoSource.setCodeSystem("AHRQ v4.3");
        cdTargetRelRoSource.setCode("C439");
        clinicalStatementRelationship.setTargetRelationshipToSource(cdTargetRelRoSource);
        clinicalStatementRelationship.setSourceId("C238");
        clinicalStatementRelationship.setTargetId("C284");

        ksession.insert(encounterEvent);
        ksession.insert(problem);
        ksession.insert(clinicalStatementRelationship);

        ksession.fireAllRules();

    }


    private KnowledgeBase buildKB( String drlPath ) {
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add( ResourceFactory.newClassPathResource(drlPath), ResourceType.DRL );
        if ( knowledgeBuilder.hasErrors() ) {
            fail( knowledgeBuilder.getErrors().toString() );
        }
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages( knowledgeBuilder.getKnowledgePackages() );
        return knowledgeBase;
    }

    private KnowledgeBase loadKnowledgeBaseFromString( String drlSource ){
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add( ResourceFactory.newByteArrayResource(drlSource.getBytes()), ResourceType.DRL );
        if ( knowledgeBuilder.hasErrors() ) {
            fail( knowledgeBuilder.getErrors().toString() );
        }
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages( knowledgeBuilder.getKnowledgePackages() );
        return knowledgeBase;

    }

}
