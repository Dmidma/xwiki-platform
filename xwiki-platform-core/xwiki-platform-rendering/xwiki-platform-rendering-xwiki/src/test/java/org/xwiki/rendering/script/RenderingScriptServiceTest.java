/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.script;

import java.io.StringReader;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xwiki.component.internal.ContextComponentManagerProvider;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.renderer.BlockRenderer;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.syntax.SyntaxFactory;
import org.xwiki.test.annotation.ComponentList;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link org.xwiki.rendering.script.RenderingScriptService}.
 *
 * @version $Id$
 * @since 3.2M3
 */
@ComponentList({ContextComponentManagerProvider.class})
public class RenderingScriptServiceTest
{
    @Rule
    public MockitoComponentMockingRule<RenderingScriptService> mocker =
        new MockitoComponentMockingRule<>(RenderingScriptService.class);

    @Test
    public void parseAndRender() throws Exception
    {
        Parser parser = this.mocker.registerMockComponent(Parser.class, "plain/1.0");
        when(parser.parse(new StringReader("some [[TODO]] stuff"))).thenReturn(
            new XDOM(Collections.<Block>emptyList()));

        BlockRenderer blockRenderer = this.mocker.registerMockComponent(BlockRenderer.class, "xwiki/2.0");
        doAnswer(new Answer() {
             @Override
             public Object answer(InvocationOnMock invocationOnMock) throws Throwable
             {
                 WikiPrinter printer = (WikiPrinter) invocationOnMock.getArguments()[1];
                 printer.print("some ~[~[TODO]] stuff");
                 return null;
                     }
             }).when(blockRenderer).render(any(XDOM.class), any(WikiPrinter.class));

        XDOM xdom = this.mocker.getComponentUnderTest().parse("some [[TODO]] stuff", "plain/1.0");
        Assert.assertEquals("some ~[~[TODO]] stuff", this.mocker.getComponentUnderTest().render(xdom, "xwiki/2.0"));
    }

    @Test
    public void parseAndRenderWhenErrorInParse() throws Exception
    {
        Parser parser = this.mocker.registerMockComponent(Parser.class, "plain/1.0");
        when(parser.parse(new StringReader("some [[TODO]] stuff"))).thenThrow(new ParseException(("error")));

        Assert.assertNull(this.mocker.getComponentUnderTest().parse("some [[TODO]] stuff", "plain/1.0"));
    }

    @Test
    public void parseAndRenderWhenErrorInRender() throws Exception
    {
        Parser parser = this.mocker.registerMockComponent(Parser.class, "plain/1.0");
        when(parser.parse(new StringReader("some [[TODO]] stuff"))).thenReturn(
                new XDOM(Collections.<Block>emptyList()));

        BlockRenderer blockRenderer = this.mocker.registerMockComponent(BlockRenderer.class, "xwiki/2.0");
        doThrow(new RuntimeException("error")).when(blockRenderer).render(any(XDOM.class), any(WikiPrinter.class));

        XDOM xdom = this.mocker.getComponentUnderTest().parse("some [[TODO]] stuff", "plain/1.0");
        Assert.assertNull(this.mocker.getComponentUnderTest().render(xdom, "xwiki/2.0"));
    }

    @Test
    public void resolveSyntax() throws Exception
    {
        SyntaxFactory syntaxFactory = this.mocker.getInstance(SyntaxFactory.class);
        when(syntaxFactory.createSyntaxFromIdString("xwiki/2.1")).thenReturn(Syntax.XWIKI_2_1);

        Assert.assertEquals(Syntax.XWIKI_2_1, this.mocker.getComponentUnderTest().resolveSyntax("xwiki/2.1"));
    }

    @Test
    public void resolveSyntaxWhenInvalid() throws Exception
    {
        SyntaxFactory syntaxFactory = this.mocker.getInstance(SyntaxFactory.class);
        when(syntaxFactory.createSyntaxFromIdString("unknown")).thenThrow(new ParseException("invalid"));

        Assert.assertNull(this.mocker.getComponentUnderTest().resolveSyntax("unknown"));
    }
}
