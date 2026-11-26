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
 * Home class for Quiz providing static service methods.
 */
public final class QuizHome
{
    private static final IQuizDAO _dao = CDI.current( ).select( IQuizDAO.class ).get( );
    private static final Plugin _plugin = PluginService.getPlugin( "wiki" );

    /**
     * Private constructor.
     */
    private QuizHome( )
    {
    }

    /**
     * Creates a new quiz in the database.
     *
     * @param quiz
     *            The quiz to create
     * @return The created quiz with its ID set
     */
    public static Quiz create( Quiz quiz )
    {
        _dao.insert( quiz, _plugin );
        return quiz;
    }

    /**
     * Updates an existing quiz in the database.
     *
     * @param quiz
     *            The quiz to update
     * @return The updated quiz
     */
    public static Quiz update( Quiz quiz )
    {
        _dao.store( quiz, _plugin );
        return quiz;
    }

    /**
     * Removes a quiz from the database.
     *
     * @param nKey
     *            The quiz ID
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Retrieves a quiz by its primary key.
     *
     * @param nKey
     *            The quiz ID
     * @return An optional containing the quiz if found
     */
    public static Optional<Quiz> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Retrieves all quizzes for a specific book.
     *
     * @param nIdBook
     *            The book ID
     * @return A list of quizzes in the book
     */
    public static List<Quiz> getQuizzesByBook( int nIdBook )
    {
        return _dao.selectByBook( nIdBook, _plugin );
    }

    /**
     * Retrieves published quizzes for a specific book.
     *
     * @param nIdBook
     *            The book ID
     * @return A list of published quizzes in the book
     */
    public static List<Quiz> getPublishedQuizzesByBook( int nIdBook )
    {
        return _dao.selectPublishedByBook( nIdBook, _plugin );
    }

}
