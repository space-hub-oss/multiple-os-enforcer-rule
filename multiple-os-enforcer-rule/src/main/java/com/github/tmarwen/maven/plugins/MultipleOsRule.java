/**
 * Copyright 2015 Wise Brains
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.tmarwen.maven.plugins;

import edu.emory.mathcs.backport.java.util.Collections;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MultipleOsRule is a subclass of the EnforcerRule interface implementing
 * a custom rule which allow specifying a range of required Operating
 * Systems instead of using the raw <requireOS></requireOS> element of the
 * enforcer-plugin which does not support range values.
 *
 * @author <a href="mailto:marwen.trabelsi.insat@gmail.com">Marwen Trabelsi</a>
 * @version 1.0
 */
public class MultipleOsRule implements EnforcerRule
{

  /**
   * Multiple OSs param. This parameter must hold a comma separated list representing the
   * range of Operating Systems where build should be allowed.
   */
  private String requiredOSs = "";

  @Override
  public void execute(EnforcerRuleHelper enforcerRuleHelper) throws EnforcerRuleException
  {
    Log log = enforcerRuleHelper.getLog();

    try
    {
      // get the OS name using the helper
      String osName = ((String) enforcerRuleHelper.evaluate("${os.name}")).toLowerCase();

      log.info("The build is running in a \"" + osName + "\" box. Will check if this OS is within the allowed range.");

      List<String> osRangeList = retrieveOsRangeList(requiredOSs);

      if (osRangeList.isEmpty())
      {
        throw new EnforcerRuleException("Allowed Operating System range should be specified.");
      } else if (!osRangeList.contains(osName))
      {
        throw new EnforcerRuleException("The current Operating System is not in the allowed range.");
      } else
      {
        log.info("--> " + osName + " is in the allowed OS range: " + osRangeList);
      }
    } catch (ExpressionEvaluationException e)
    {
      throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(), e);
    }
  }

  @Override
  public boolean isCacheable()
  {
    return false;
  }

  @Override
  public boolean isResultValid(EnforcerRule enforcerRule)
  {
    return false;
  }

  @Override
  public String getCacheId()
  {
    return null;
  }

  private List<String> retrieveOsRangeList(String allowedOSs)
  {
    List<String> allowedOSsList;
    if (StringUtils.isBlank(allowedOSs))
    {
      allowedOSsList = Collections.emptyList();
    } else
    {
      String[] oss = allowedOSs.split(",");
      allowedOSsList = new ArrayList<String>();
      for (int i = 0; i < oss.length; i++)
      {
        allowedOSsList.add(oss[i].toLowerCase().trim());
      }
    }
    return allowedOSsList;
  }
}
