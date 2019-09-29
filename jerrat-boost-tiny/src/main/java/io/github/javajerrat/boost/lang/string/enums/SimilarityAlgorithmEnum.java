/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.github.javajerrat.boost.lang.string.enums;

import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.HammingDistance;
import org.apache.commons.text.similarity.JaccardDistance;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.text.similarity.LevenshteinResults;
import org.apache.commons.text.similarity.LongestCommonSubsequence;
import org.apache.commons.text.similarity.LongestCommonSubsequenceDistance;
import org.apache.commons.text.similarity.SimilarityScore;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/17
 */
public abstract class SimilarityAlgorithmEnum<T> {


    /**
     * 余弦相似性(cosine similarity）
     **/
    public static final SimilarityAlgorithmEnum<Double> COSINE_DISTANCE = new SimilarityAlgorithmEnum<Double>() {

        @Override
        public SimilarityScore<Double> create() {
            return new CosineDistance();
        }
    };

    /**
     * 海明距离（hamming distance）
     */
    public static final SimilarityAlgorithmEnum<Integer> HAMMING_DISTANCE = new SimilarityAlgorithmEnum<Integer>() {
        @Override
        public SimilarityScore<Integer> create() {
            return new HammingDistance();
        }
    };

    /**
     * 杰卡德距离(jaccard distance)
     */
    public static final SimilarityAlgorithmEnum<Double> JACCARD_DISTANCE = new SimilarityAlgorithmEnum<Double>() {
        @Override
        public SimilarityScore<Double> create() {
            return new JaccardDistance();
        }
    };

    /**
     * 杰卡德相似性(jaccard similarity)
     */
    public static final SimilarityAlgorithmEnum<Double> JACCARD_SIMILARITY = new SimilarityAlgorithmEnum<Double>() {
        @Override
        public SimilarityScore<Double> create() {
            return new JaccardSimilarity();
        }
    };

    /**
     * J-W距离（Jaro–Winkler distance）
     */
    public static final SimilarityAlgorithmEnum<Double> JARO_WINKLER_DISTANCE = new SimilarityAlgorithmEnum<Double>() {
        @Override
        public SimilarityScore<Double> create() {
            return new JaroWinklerDistance();
        }
    };

    /**
     * 编辑距离（这个算法能拿到更详细的结果）
     */
    public static final SimilarityAlgorithmEnum<LevenshteinResults> LEVENSHTEIN_DETAILED_DISTANCE = new SimilarityAlgorithmEnum<LevenshteinResults>() {
        @Override
        public SimilarityScore<LevenshteinResults> create() {
            return new LevenshteinDetailedDistance();
        }
    };


    /**
     * 编辑距离(Levenshtein distance)
     */
    public static final SimilarityAlgorithmEnum<Integer> LEVENSHTEIN_DISTANCE = new SimilarityAlgorithmEnum<Integer>() {
        @Override
        public SimilarityScore<Integer> create() {
            return new LevenshteinDistance();
        }
    };

    /**
     * 最长公共子序列(longest common subsequence)
     */
    public static final SimilarityAlgorithmEnum<Integer> LONGEST_COMMON_SUBSEQUENCE = new SimilarityAlgorithmEnum<Integer>() {
        @Override
        public SimilarityScore<Integer> create() {
            return new LongestCommonSubsequence();
        }
    };

    /**
     * 最长公共子序列距离(longest common subsequence distance)
     */
    public static final SimilarityAlgorithmEnum<Integer> LONGEST_COMMON_SUBSEQUENCE_DISTANCE = new SimilarityAlgorithmEnum<Integer>() {
        @Override
        public SimilarityScore<Integer> create() {
            return new LongestCommonSubsequenceDistance();
        }
    };

    public abstract SimilarityScore<T> create();
}
