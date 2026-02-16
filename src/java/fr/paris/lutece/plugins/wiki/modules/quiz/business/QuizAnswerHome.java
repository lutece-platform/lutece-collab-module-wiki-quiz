/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.wiki.modules.quiz.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import jakarta.enterprise.inject.spi.CDI;

import java.util.List;

/**
 * Home class for QuizAnswer providing static service methods.
 */
public final class QuizAnswerHome
{
    private static final IQuizAnswerDAO _dao = CDI.current( ).select( IQuizAnswerDAO.class ).get( );
    private static final Plugin _plugin = PluginService.getPlugin( "wiki" );

    /**
     * Private constructor.
     */
    private QuizAnswerHome( )
    {
    }

    /**
     * Creates a new quiz answer in the database.
     *
     * @param answer
     *            The quiz answer to create
     * @return The created quiz answer with its ID set
     */
    public static QuizAnswer create( QuizAnswer answer )
    {
        _dao.insert( answer, _plugin );
        return answer;
    }

    /**
     * Removes all answers for a specific question.
     *
     * @param nIdQuestion
     *            The question ID
     */
    public static void removeByQuestion( int nIdQuestion )
    {
        _dao.deleteByQuestion( nIdQuestion, _plugin );
    }

    /**
     * Retrieves all answers for a specific question.
     *
     * @param nIdQuestion
     *            The question ID
     * @return A list of quiz answers for the question
     */
    public static List<QuizAnswer> getAnswersByQuestion( int nIdQuestion )
    {
        return _dao.selectByQuestion( nIdQuestion, _plugin );
    }
}
