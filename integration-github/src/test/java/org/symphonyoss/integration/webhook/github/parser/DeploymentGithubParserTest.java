package org.symphonyoss.integration.webhook.github.parser;

import static java.util.Collections.EMPTY_MAP;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.symphonyoss.integration.json.JsonUtils;
import org.symphonyoss.integration.webhook.github.CommonGithubTest;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

/**
 * Unit tests for {@link DeploymentGithubParser}
 *
 * Created by rsanchez on 23/09/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeploymentGithubParserTest extends CommonGithubTest {

  private static final String USER_URL = "https://api.github.com/users/baxterthehacker";

  @Mock
  private GithubParserUtils utils;

  @InjectMocks
  private DeploymentGithubParser parser = new DeploymentGithubParser();

  @Test
  public void testDeploymentEvent() throws IOException, GithubParserException {
    JsonNode publicUserInfoBaxter = JsonUtils.readTree(
        classLoader.getResourceAsStream("payload_github_public_info_baxterthehacker.json"));
    doReturn(publicUserInfoBaxter).when(utils).doGetJsonApi(USER_URL);

    JsonNode node = JsonUtils.readTree(
        classLoader.getResourceAsStream("payload_xgithubevent_deployment.json"));

    String expected = readFile("payload_xgithubevent_deployment_expected_message.xml");
    String result = "<messageML>" + parser.parse(EMPTY_MAP, node) + "</messageML>";

    assertEquals(expected, result);
  }

  @Test
  public void testDeploymentEventWithoutUserInfoAndDescription() throws IOException, GithubParserException {
    doReturn(null).when(utils).doGetJsonApi(USER_URL);

    JsonNode node = JsonUtils.readTree(classLoader.getResourceAsStream(
        "payload_xgithubevent_deployment_without_description.json"));

    String expected =
        readFile("payload_xgithubevent_deployment_without_userinfo_expected_message.xml");
    String result = "<messageML>" + parser.parse(EMPTY_MAP, node) + "</messageML>";

    assertEquals(expected, result);
  }

}
