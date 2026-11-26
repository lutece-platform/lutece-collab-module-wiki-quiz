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
 * Data access object interface for quiz attempts.
 */
public interface IQuizAttemptDAO
{
    /**
     * Inserts a new quiz attempt into the database.
     *
     * @param attempt
     *            The quiz attempt to insert
     * @param plugin
     *            The plugin instance
     */
    void insert( QuizAttempt attempt, Plugin plugin );

    /**
     * Updates an existing quiz attempt in the database.
     *
     * @param attempt
     *            The quiz attempt to update
     * @param plugin
     *            The plugin instance
     */
    void store( QuizAttempt attempt, Plugin plugin );

    /**
     * Loads a quiz attempt by its primary key.
     *
     * @param nKey
     *            The attempt ID
     * @param plugin
     *            The plugin instance
     * @return An optional containing the quiz attempt if found
     */
    Optional<QuizAttempt> load( int nKey, Plugin plugin );

    /**
     * Retrieves all attempts for a specific quiz.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @param plugin
     *            The plugin instance
     * @return A list of quiz attempts for the quiz
     */
    List<QuizAttempt> selectByQuiz( int nIdQuiz, Plugin plugin );

    /**
     * Retrieves all attempts for a specific quiz by a specific user.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @param strUserGuid
     *            The user GUID
     * @param plugin
     *            The plugin instance
     * @return A list of quiz attempts for the quiz and user
     */
    List<QuizAttempt> selectByQuizAndUser( int nIdQuiz, String strUserGuid, Plugin plugin );

    /**
     * Counts the number of attempts for a specific quiz by a specific user.
     *
     * @param nIdQuiz
     *            The quiz ID
     * @param strUserGuid
     *            The user GUID
     * @param plugin
     *            The plugin instance
     * @return The count of attempts
     */
    int countByQuizAndUser( int nIdQuiz, String strUserGuid, Plugin plugin );
}
