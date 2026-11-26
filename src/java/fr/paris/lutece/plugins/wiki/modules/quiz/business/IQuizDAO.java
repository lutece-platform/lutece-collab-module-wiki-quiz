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

import java.util.List;
import java.util.Optional;

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * Data access object interface for quizzes.
 */
public interface IQuizDAO
{
    /**
     * Inserts a new quiz into the database.
     *
     * @param quiz
     *            The quiz to insert
     * @param plugin
     *            The plugin instance
     */
    void insert( Quiz quiz, Plugin plugin );

    /**
     * Updates an existing quiz in the database.
     *
     * @param quiz
     *            The quiz to update
     * @param plugin
     *            The plugin instance
     */
    void store( Quiz quiz, Plugin plugin );

    /**
     * Deletes a quiz by its primary key.
     *
     * @param nKey
     *            The quiz ID
     * @param plugin
     *            The plugin instance
     */
    void delete( int nKey, Plugin plugin );

    /**
     * Loads a quiz by its primary key.
     *
     * @param nKey
     *            The quiz ID
     * @param plugin
     *            The plugin instance
     * @return An optional containing the quiz if found
     */
    Optional<Quiz> load( int nKey, Plugin plugin );

    /**
     * Retrieves all quizzes for a specific book.
     *
     * @param nIdBook
     *            The book ID
     * @param plugin
     *            The plugin instance
     * @return A list of quizzes in the book
     */
    List<Quiz> selectByBook( int nIdBook, Plugin plugin );

    /**
     * Retrieves published quizzes for a specific book.
     *
     * @param nIdBook
     *            The book ID
     * @param plugin
     *            The plugin instance
     * @return A list of published quizzes in the book
     */
    List<Quiz> selectPublishedByBook( int nIdBook, Plugin plugin );

}
