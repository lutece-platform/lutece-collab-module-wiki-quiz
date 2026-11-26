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
import java.util.Optional;

/**
 * Home class for QuizResponse providing static service methods.
 */
public final class QuizResponseHome
{
    private static final IQuizResponseDAO _dao = CDI.current( ).select( IQuizResponseDAO.class ).get( );
    private static final Plugin _plugin = PluginService.getPlugin( "wiki" );

    /**
     * Private constructor.
     */
    private QuizResponseHome( )
    {
    }

    /**
     * Creates a new quiz response in the database.
     *
     * @param response
     *            The quiz response to create
     * @return The created quiz response with its ID set
     */
    public static QuizResponse create( QuizResponse response )
    {
        _dao.insert( response, _plugin );
        return response;
    }

    /**
     * Updates an existing quiz response in the database.
     *
     * @param response
     *            The quiz response to update
     * @return The updated quiz response
     */
    public static QuizResponse update( QuizResponse response )
    {
        _dao.store( response, _plugin );
        return response;
    }

    /**
     * Retrieves all responses for a specific attempt.
     *
     * @param nIdAttempt
     *            The attempt ID
     * @return A list of quiz responses for the attempt
     */
    public static List<QuizResponse> getResponsesByAttempt( int nIdAttempt )
    {
        return _dao.selectByAttempt( nIdAttempt, _plugin );
    }

    /**
     * Retrieves a response for a specific attempt and question combination.
     *
     * @param nIdAttempt
     *            The attempt ID
     * @param nIdQuestion
     *            The question ID
     * @return An optional containing the quiz response if found
     */
    public static Optional<QuizResponse> findByAttemptAndQuestion( int nIdAttempt, int nIdQuestion )
    {
        return _dao.selectByAttemptAndQuestion( nIdAttempt, nIdQuestion, _plugin );
    }
}
