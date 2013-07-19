package org.jboss.narayana.rest.bridge.inbound.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jbossts.star.util.TxLinkNames;
import org.jboss.narayana.rest.bridge.inbound.EJBExceptionMapper;
import org.jboss.narayana.rest.bridge.inbound.TransactionalExceptionMapper;
import org.jboss.narayana.rest.bridge.inbound.test.common.ResourceWithTransactionAttributeAnnotation;
import org.jboss.narayana.rest.bridge.inbound.test.common.ResourceWithTransactionalAnnotation;
import org.jboss.narayana.rest.bridge.inbound.test.common.ResourceWitoutAnnotation;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Link;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@RunWith(Arquillian.class)
public class AnnotationsTestCase extends AbstractTestCase {

    private static final String NO_ANNOTATION_URL = DEPLOYMENT_URL + "/" + ResourceWitoutAnnotation.URL_SEGMENT;

    private static final String TRANSACTIONAL_MANDATORY_URL = DEPLOYMENT_URL + "/"
            + ResourceWithTransactionalAnnotation.URL_SEGMENT + "/"
            + ResourceWithTransactionalAnnotation.MANDATORY_SEGMENT;

    private static final String TRANSACTIONAL_NEVER_URL = DEPLOYMENT_URL + "/"
            + ResourceWithTransactionalAnnotation.URL_SEGMENT + "/"
            + ResourceWithTransactionalAnnotation.NEVER_SEGMENT;

    private static final String TRANSACTION_ATTRIBUTE_MANDATORY_URL = DEPLOYMENT_URL + "/"
            + ResourceWithTransactionAttributeAnnotation.URL_SEGMENT + "/"
            + ResourceWithTransactionAttributeAnnotation.MANDATORY_SEGMENT;

    private static final String TRANSACTION_ATTRIBUTE_NEVER_URL = DEPLOYMENT_URL + "/"
            + ResourceWithTransactionAttributeAnnotation.URL_SEGMENT + "/"
            + ResourceWithTransactionAttributeAnnotation.NEVER_SEGMENT;

    @Deployment(name = DEPLOYMENT_NAME, testable = false, managed = false)
    public static WebArchive createDeployment() {
        return getEmptyWebArchive()
                .addClasses(ResourceWitoutAnnotation.class, ResourceWithTransactionalAnnotation.class,
                        ResourceWithTransactionAttributeAnnotation.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web.xml", "web.xml");
    }

    @Before
    public void before() {
        super.before();
        startContainer();
    }

    @After
    public void after() {
        super.after();
        stopContainer();
    }

    @Test
    public void testNoAnnotationWithTransaction() {
        txSupport.startTx();
        final ClientResponse<String> response = postRestATResource(NO_ANNOTATION_URL);
        txSupport.commitTx();
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testTransactionalMandatoryWithTransaction() throws Exception {
        txSupport.startTx();
        final ClientResponse<String> response = postRestATResource(TRANSACTIONAL_MANDATORY_URL);
        txSupport.commitTx();
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testTransactionalMandatoryWithoutTransaction() throws Exception {
        final ClientResponse response = new ClientRequest(TRANSACTIONAL_MANDATORY_URL).post(String.class);
        Assert.assertEquals(412, response.getStatus());
        Assert.assertEquals(TransactionalExceptionMapper.TRANSACTIONA_REQUIRED_MESSAGE, response.getEntity());
    }

    @Test
    public void testTransactionalNeverWithTransaction() throws Exception {
        txSupport.startTx();
        final ClientResponse<String> response = postRestATResource(TRANSACTIONAL_NEVER_URL);
        txSupport.commitTx();
        Assert.assertEquals(412, response.getStatus());
        Assert.assertEquals(TransactionalExceptionMapper.INVALID_TRANSACTIONA_MESSAGE, response.getEntity());
    }

    @Test
    public void testTransactionalNeverWithoutTransaction() throws Exception {
        final ClientResponse response = new ClientRequest(TRANSACTIONAL_NEVER_URL).post(String.class);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testTransactionAttributeMandatoryWithTransaction() throws Exception {
        txSupport.startTx();
        final ClientResponse<String> response = postRestATResource(TRANSACTION_ATTRIBUTE_MANDATORY_URL);
        txSupport.commitTx();
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testTransactionAttributeMandatoryWithoutTransaction() throws Exception {
        final ClientResponse response = new ClientRequest(TRANSACTION_ATTRIBUTE_MANDATORY_URL).post(String.class);
        Assert.assertEquals(412, response.getStatus());
        Assert.assertEquals(EJBExceptionMapper.TRANSACTIONA_REQUIRED_MESSAGE, response.getEntity());
    }

    @Test
    public void testTransactionAttributeNeverWithTransaction() throws Exception {
        txSupport.startTx();
        final ClientResponse<String> response = postRestATResource(TRANSACTION_ATTRIBUTE_NEVER_URL);
        txSupport.commitTx();
        Assert.assertEquals(412, response.getStatus());
        Assert.assertEquals(EJBExceptionMapper.INVALID_TRANSACTIONA_MESSAGE, response.getEntity());
    }

    @Test
    public void testTransactionAttributeNeverWithoutTransaction() throws Exception {
        final ClientResponse response = new ClientRequest(TRANSACTION_ATTRIBUTE_NEVER_URL).post(String.class);
        Assert.assertEquals(200, response.getStatus());
    }

    protected ClientResponse<String> postRestATResource(final String resourceUrl) {
        final Link participantLink = Link.fromUri(txSupport.getTxnUri()).rel(TxLinkNames.PARTICIPANT)
                .title(TxLinkNames.PARTICIPANT).build();

        try {
            return new ClientRequest(resourceUrl).header("link", participantLink).post(String.class);
        } catch (Exception e) {
            return null;
        }
    }

}
