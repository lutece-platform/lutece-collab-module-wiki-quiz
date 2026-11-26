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
 * Data access object interface for quiz questions.
 */
public interface IQuizQuestionDAO
{
    /**
     * Inserts a new quiz question into the database.
     *
     * @param question
     *            The quiz question to insert
     * @param plugin
     *            The plugin instance
     */
    void insert( QuizQuestion question, Plugin plugin );

    /**
     * Updates an existing quiz question in the database.
     *
     * @param question
     *            The quiz question to update
     * @param plugin
     *            The plugin instance
     */
    void store( QuizQuestion question, Plugin plugin );

    /**
     * Deletes a quiz question by its primary key.
     *
     * @param nKey
     *            The question ID
     * @param plugin
     *            The plugin instance
     */
    void delete( int nKey, Plugin plugin );

    /**
     * Loads a quiz question by its primary key.
     *
     * @param nKey
     *            The question ID
     * @param plugin
     *            The plugin instance
     * @return An optional containing the quiz question if found
     */
    Optional<QuizQuestion> load( int nKey, Plugin plugin );

    /**
     * Retrieves all questions for a specific quiz.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @param plugin
     *            The plugin instance
     * @return A list of quiz questions for the quiz
     */
    List<QuizQuestion> selectByQuiz( int nIdQuiz, Plugin plugin );

    /**
     * Gets the maximum display order for questions in a specific quiz.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @param plugin
     *            The plugin instance
     * @return The maximum display order
     */
    int getMaxDisplayOrder( int nIdQuiz, Plugin plugin );

    /**
     * Retrieves the IDs of pages that are sources for a question.
     *
     * @param nIdQuestion
     *            The question ID
     * @param plugin
     *            The plugin instance
     * @return A list of source page IDs
     */
    List<Integer> selectSourcePageIds( int nIdQuestion, Plugin plugin );

    /**
     * Inserts links between a question and a list of source pages.
     *
     * @param nIdQuestion
     *            The question ID
     * @param listPageIds
     *            The list of page IDs to link
     * @param plugin
     *            The plugin instance
     */
    void insertSourcePages( int nIdQuestion, List<Integer> listPageIds, Plugin plugin );

    /**
     * Deletes all source page links for a specific question.
     *
     * @param nIdQuestion
     *            The question ID
     * @param plugin
     *            The plugin instance
     */
    void deleteSourcePages( int nIdQuestion, Plugin plugin );
}
