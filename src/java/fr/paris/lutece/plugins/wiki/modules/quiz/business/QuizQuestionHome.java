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
 * Home class for QuizQuestion providing static service methods.
 */
public final class QuizQuestionHome
{
    private static final IQuizQuestionDAO _dao = CDI.current( ).select( IQuizQuestionDAO.class ).get( );
    private static final Plugin _plugin = PluginService.getPlugin( "wiki" );

    /**
     * Private constructor.
     */
    private QuizQuestionHome( )
    {
    }

    /**
     * Creates a new quiz question in the database.
     *
     * @param question
     *            The quiz question to create
     * @return The created quiz question with its ID set
     */
    public static QuizQuestion create( QuizQuestion question )
    {
        _dao.insert( question, _plugin );
        return question;
    }

    /**
     * Updates an existing quiz question in the database.
     *
     * @param question
     *            The quiz question to update
     * @return The updated quiz question
     */
    public static QuizQuestion update( QuizQuestion question )
    {
        _dao.store( question, _plugin );
        return question;
    }

    /**
     * Removes a quiz question from the database.
     *
     * @param nKey
     *            The question ID
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Retrieves a quiz question by its primary key.
     *
     * @param nKey
     *            The question ID
     * @return An optional containing the quiz question if found
     */
    public static Optional<QuizQuestion> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Retrieves all questions for a specific quiz.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @return A list of quiz questions for the quiz
     */
    public static List<QuizQuestion> getQuestionsByQuiz( int nIdQuiz )
    {
        return _dao.selectByQuiz( nIdQuiz, _plugin );
    }

    /**
     * Gets the next display order for a new question in a quiz.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @return The next display order
     */
    public static int getNextDisplayOrder( int nIdQuiz )
    {
        return _dao.getMaxDisplayOrder( nIdQuiz, _plugin ) + 1;
    }

    /**
     * Retrieves the IDs of pages that are sources for a question.
     *
     * @param nIdQuestion
     *            The question ID
     * @return A list of source page IDs
     */
    public static List<Integer> getSourcePageIds( int nIdQuestion )
    {
        return _dao.selectSourcePageIds( nIdQuestion, _plugin );
    }

    /**
     * Sets the source pages for a question, replacing any existing links.
     *
     * @param nIdQuestion
     *            The question ID
     * @param listPageIds
     *            The list of page IDs to link
     */
    public static void setSourcePages( int nIdQuestion, List<Integer> listPageIds )
    {
        _dao.deleteSourcePages( nIdQuestion, _plugin );
        if ( listPageIds != null && !listPageIds.isEmpty( ) )
        {
            _dao.insertSourcePages( nIdQuestion, listPageIds, _plugin );
        }
    }

}
