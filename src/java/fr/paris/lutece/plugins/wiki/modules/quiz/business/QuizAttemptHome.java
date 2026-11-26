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
 * Home class for QuizAttempt providing static service methods.
 */
public final class QuizAttemptHome
{
    private static final IQuizAttemptDAO _dao = CDI.current( ).select( IQuizAttemptDAO.class ).get( );
    private static final Plugin _plugin = PluginService.getPlugin( "wiki" );

    /**
     * Private constructor.
     */
    private QuizAttemptHome( )
    {
    }

    /**
     * Creates a new quiz attempt in the database.
     *
     * @param attempt
     *            The quiz attempt to create
     * @return The created quiz attempt with its ID set
     */
    public static QuizAttempt create( QuizAttempt attempt )
    {
        _dao.insert( attempt, _plugin );
        return attempt;
    }

    /**
     * Updates an existing quiz attempt in the database.
     *
     * @param attempt
     *            The quiz attempt to update
     * @return The updated quiz attempt
     */
    public static QuizAttempt update( QuizAttempt attempt )
    {
        _dao.store( attempt, _plugin );
        return attempt;
    }

    /**
     * Retrieves a quiz attempt by its primary key.
     *
     * @param nKey
     *            The attempt ID
     * @return An optional containing the quiz attempt if found
     */
    public static Optional<QuizAttempt> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Retrieves all attempts for a specific quiz.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @return A list of quiz attempts for the quiz
     */
    public static List<QuizAttempt> getAttemptsByQuiz( int nIdQuiz )
    {
        return _dao.selectByQuiz( nIdQuiz, _plugin );
    }

    /**
     * Retrieves all attempts for a specific quiz by a specific user.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @param strUserGuid
     *            The user GUID
     * @return A list of quiz attempts for the quiz and user
     */
    public static List<QuizAttempt> getAttemptsByQuizAndUser( int nIdQuiz, String strUserGuid )
    {
        return _dao.selectByQuizAndUser( nIdQuiz, strUserGuid, _plugin );
    }

    /**
     * Counts the number of attempts for a specific quiz by a specific user.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @param strUserGuid
     *            The user GUID
     * @return The count of attempts
     */
    public static int countAttemptsByQuizAndUser( int nIdQuiz, String strUserGuid )
    {
        return _dao.countByQuizAndUser( nIdQuiz, strUserGuid, _plugin );
    }
}
